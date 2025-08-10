#!/usr/bin/env node

import { Server } from '@modelcontextprotocol/sdk/server/index.js';
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js';
import { CallToolRequestSchema, ListToolsRequestSchema } from '@modelcontextprotocol/sdk/types.js';
import { MusinsaAPI } from './musinsa-api.js';
import { MusinsaReviewAPI } from './musinsa-review-api.js';

const server = new Server(
  {
    name: 'choavis-mcp',
    version: '1.0.0',
  },
  {
    capabilities: {
      tools: {},
    },
  }
);

const musinsaAPI = new MusinsaAPI();
const musinsaReviewAPI = new MusinsaReviewAPI();

server.setRequestHandler(ListToolsRequestSchema, async () => {
  return {
    tools: [
      {
        name: 'search_musinsa_products',
        description: '무신사에서 패션 상품을 검색합니다. 사용자의 자연어 요청을 받아서 Claude가 적절한 검색 키워드를 추출하여 검색을 수행합니다. 무신사는 키워드 매칭 방식이므로 정확한 상품명/카테고리명만 사용해야 합니다. 검색 결과를 바탕으로 사용자에게 맞춤형 추천과 분석을 제공해야 합니다. 중요: 검색 결과에 포함된 구매 링크를 반드시 그대로 사용자에게 제공해야 합니다.',
        inputSchema: {
          type: 'object',
          properties: {
            user_request: {
              type: 'string',
              description: '사용자의 원본 자연어 요청. 예: "지금 너무 더운데 시원하게 입을 반바지좀 추천해줘", "정장에 어울리는 구두 찾아줘", "데이트에 입을 예쁜 원피스" 등',
            },
            search_keywords: {
              type: 'string',
              description: 'Claude가 사용자 요청을 분석하여 추출한 검색 키워드. 무신사는 키워드 매칭 방식이므로 정확한 상품명/카테고리명을 사용해야 합니다. 형용사나 수식어는 제거하고 핵심 키워드만 추출하세요.\n\n키워드 추출 원칙:\n1. 핵심 상품명만 추출 (반바지, 티셔츠, 운동화, 가방 등)\n2. 형용사 제거 (시원한, 예쁜, 편한, 따뜻한 등)\n3. 상황 설명 제거 (데이트에, 운동할 때, 회사에서 등)\n4. 2-3개 키워드 조합 가능 (여름 반바지, 정장 구두)\n\n올바른 예시:\n- "지금 너무 더운데 시원하게 입을 반바지" → "반바지"\n- "정장에 어울리는 검은 구두" → "정장 구두"\n- "데이트에 입을 예쁜 원피스" → "원피스"\n- "운동할 때 편한 운동화" → "운동화"\n- "겨울에 따뜻한 패딩" → "패딩"\n- "회사에서 입을 깔끔한 셔츠" → "셔츠"\n\n잘못된 예시:\n- "시원한 반바지" (X)\n- "편안한 운동화" (X)\n- "예쁜 원피스" (X)\n- "따뜻한 겨울 패딩" (X)',
            },
            gender: {
              type: 'string',
              enum: ['A', 'M', 'F'],
              description: '성별 필터 (A: 전체, M: 남성, F: 여성)',
              default: 'A',
            },
            sortCode: {
              type: 'string',
              enum: ['POPULAR', 'SALE', 'PRICE_LOW', 'PRICE_HIGH', 'NEW'],
              description: '정렬 기준 (POPULAR: 인기순, SALE: 할인율순, PRICE_LOW: 낮은가격순, PRICE_HIGH: 높은가격순, NEW: 신상품순)',
              default: 'POPULAR',
            },
            page: {
              type: 'number',
              description: '페이지 번호',
              default: 1,
            },
            size: {
              type: 'number',
              description: '페이지당 상품 수',
              default: 60,
            },
          },
          required: ['user_request', 'search_keywords'],
        },
      },
      {
        name: 'get_musinsa_product_reviews',
        description: '무신사 상품의 리뷰를 조회합니다. goodsNo(상품번호)를 통해 해당 상품의 리뷰를 가져올 수 있으며, 평점 분포, 리뷰 내용, 사용자 정보 등을 확인할 수 있습니다.',
        inputSchema: {
          type: 'object',
          properties: {
            goodsNo: {
              type: 'number',
              description: '리뷰를 조회할 상품 번호',
            },
            page: {
              type: 'number',
              description: '페이지 번호 (0부터 시작)',
              default: 0,
            },
            pageSize: {
              type: 'number',
              description: '페이지당 리뷰 수 (최대 400)',
              default: 100,
            },
            sort: {
              type: 'string',
              enum: ['up_cnt_desc', 'date_desc', 'grade_desc', 'grade_asc'],
              description: '정렬 기준 (up_cnt_desc: 도움된순, date_desc: 최신순, grade_desc: 평점높은순, grade_asc: 평점낮은순)',
              default: 'up_cnt_desc',
            },
            hasPhoto: {
              type: 'boolean',
              description: '사진 리뷰만 보기',
              default: false,
            },
            selectedSimilarNo: {
              type: 'number',
              description: '선택된 유사 상품 번호 (일반적으로 goodsNo와 동일)',
            },
          },
          required: ['goodsNo'],
        },
      },
    ],
  };
});

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const { name, arguments: args } = request.params;

  if (name === 'search_musinsa_products') {
    try {
      const { user_request, search_keywords, gender = 'A', sortCode = 'POPULAR', page = 1, size = 60 } = args as {
        user_request: string;
        search_keywords: string;
        gender?: string;
        sortCode?: string;
        page?: number;
        size?: number;
      };

      const searchParams = {
        keyword: search_keywords,
        gf: gender,
        sortCode,
        page,
        size,
        caller: 'SEARCH',
      };

      const results = await musinsaAPI.searchProducts(searchParams);
      const formattedResults = musinsaAPI.formatProductsForDisplay(results);

      const searchSummary = `
🔍 **검색 정보**
- 사용자 요청: "${user_request}"
- 검색 키워드: "${search_keywords}"
- 검색 결과: ${results.data.list?.length || 0}개 상품
- 성별 필터: ${gender === 'A' ? '전체' : gender === 'M' ? '남성' : '여성'}
- 정렬 기준: ${sortCode}

⚠️ Claude에게 중요한 지침: 아래 검색 결과에 포함된 "바로 구매하기" 링크를 반드시 그대로 사용자에게 표시해주세요. 링크를 제거하거나 수정하지 마세요.
`;

      return {
        content: [
          {
            type: 'text',
            text: searchSummary + '\n' + formattedResults,
          },
        ],
      };
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Unknown error occurred';
      return {
        content: [
          {
            type: 'text',
            text: `무신사 상품 검색 중 오류가 발생했습니다: ${errorMessage}`,
          },
        ],
        isError: true,
      };
    }
  }

  if (name === 'get_musinsa_product_reviews') {
    try {
      const { goodsNo, page = 0, pageSize = 100, sort = 'up_cnt_desc', hasPhoto = false, selectedSimilarNo } = args as {
        goodsNo: number;
        page?: number;
        pageSize?: number;
        sort?: 'up_cnt_desc' | 'date_desc' | 'grade_desc' | 'grade_asc';
        hasPhoto?: boolean;
        selectedSimilarNo?: number;
      };

      const reviewParams = {
        goodsNo,
        page,
        pageSize,
        sort,
        hasPhoto,
        selectedSimilarNo: selectedSimilarNo || goodsNo,
        myFilter: false,
        isExperience: false,
      };

      const reviews = await musinsaReviewAPI.getProductReviews(reviewParams);
      const formattedReviews = musinsaReviewAPI.formatReviewsForDisplay(reviews);

      const reviewSummary = `
🔍 **리뷰 조회 정보**
- 상품번호: ${goodsNo}
- 정렬 기준: ${sort === 'up_cnt_desc' ? '도움된순' : sort === 'date_desc' ? '최신순' : sort === 'grade_desc' ? '평점높은순' : '평점낮은순'}
- 사진 리뷰만: ${hasPhoto ? '예' : '아니오'}
- 페이지: ${page + 1} (총 ${reviews.data.page.totalPages}페이지)
`;

      return {
        content: [
          {
            type: 'text',
            text: reviewSummary + '\n' + formattedReviews,
          },
        ],
      };
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Unknown error occurred';
      return {
        content: [
          {
            type: 'text',
            text: `무신사 리뷰 조회 중 오류가 발생했습니다: ${errorMessage}`,
          },
        ],
        isError: true,
      };
    }
  }

  throw new Error(`Unknown tool: ${name}`);
});

async function main() {
  const args = process.argv.slice(2);
  
  // --stdio 플래그 확인 (선택사항, 기본적으로 stdio 사용)
  const useStdio = args.includes('--stdio') || args.length === 0;
  
  if (!useStdio) {
    console.error('Choavis MCP server supports only stdio transport');
    process.exit(1);
  }

  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error('Choavis MCP server running on stdio');
}

main().catch((error) => {
  console.error('Server error:', error);
  process.exit(1);
});