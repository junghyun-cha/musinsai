import { MusinsaReviewApiResponse, ReviewSearchParams, MusinsaReview } from './types.js';
import fetch from 'node-fetch';

export class MusinsaReviewAPI {
  private baseUrl = 'https://goods.musinsa.com/api2/review/v1/view/list';

  async getProductReviews(params: ReviewSearchParams): Promise<MusinsaReviewApiResponse> {
    const searchParams = new URLSearchParams();
    
    // 필수 파라미터
    searchParams.append('goodsNo', params.goodsNo.toString());
    
    // 선택적 파라미터
    if (params.page !== undefined) searchParams.append('page', params.page.toString());
    if (params.pageSize !== undefined) searchParams.append('pageSize', params.pageSize.toString());
    if (params.sort) searchParams.append('sort', params.sort);
    if (params.selectedSimilarNo !== undefined) searchParams.append('selectedSimilarNo', params.selectedSimilarNo.toString());
    if (params.myFilter !== undefined) searchParams.append('myFilter', params.myFilter.toString());
    if (params.hasPhoto !== undefined) searchParams.append('hasPhoto', params.hasPhoto.toString());
    if (params.isExperience !== undefined) searchParams.append('isExperience', params.isExperience.toString());

    const url = `${this.baseUrl}?${searchParams.toString()}`;
    
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json() as MusinsaReviewApiResponse;
      return data;
    } catch (error) {
      console.error('Error fetching from Musinsa Review API:', error);
      throw error;
    }
  }

  formatReviewsForDisplay(reviews: MusinsaReviewApiResponse): string {
    if (!reviews.data.list || reviews.data.list.length === 0) {
      return "이 상품에 대한 리뷰가 없습니다.";
    }

    const reviewList = reviews.data.list.slice(0, 10).map((review, index) => {
      const rating = this.getStarRating(parseInt(review.grade));
      const userInfo = review.userProfileInfo;
      const userSpec = userInfo.userHeight && userInfo.userWeight 
        ? ` (${userInfo.userHeight}cm/${userInfo.userWeight}kg)`
        : '';
      const photos = review.images && review.images.length > 0 
        ? ` 📷 사진 ${review.images.length}장` 
        : '';
      const helpful = review.likeCount > 0 ? ` 👍 도움됨 ${review.likeCount}` : '';
      
      return `${index + 1}. **${rating} (${review.grade}/5)**
   📝 "${this.truncateContent(review.content, 150)}"
   • 작성자: ${userInfo.userNickName}${userSpec}
   • 사이즈: ${review.goodsOption}
   • 작성일: ${this.formatDate(review.createDate)}${photos}${helpful}`;
    }).join('\n\n---\n\n');

    const avgGrade = this.calculateAverageGrade(reviews.data.list);
    const gradeDistribution = this.getGradeDistribution(reviews.data.list);

    return `📊 **${reviews.data.list[0]?.goods.goodsName || '상품'} 리뷰** (총 ${reviews.data.total}개)

⭐ **평균 평점: ${avgGrade.toFixed(1)}/5.0**
${gradeDistribution}

**최근 리뷰 10개**
${reviewList}

🔗 더 많은 리뷰는 무신사 상품 페이지에서 확인하세요.`;
  }

  private getStarRating(grade: number): string {
    return '⭐'.repeat(grade) + '☆'.repeat(5 - grade);
  }

  private truncateContent(content: string, maxLength: number): string {
    const cleanContent = content.replace(/\n/g, ' ').trim();
    if (cleanContent.length <= maxLength) return cleanContent;
    return cleanContent.substring(0, maxLength) + '...';
  }

  private formatDate(dateString: string): string {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}.${month}.${day}`;
  }

  private calculateAverageGrade(reviews: MusinsaReview[]): number {
    if (reviews.length === 0) return 0;
    const sum = reviews.reduce((acc, review) => acc + parseInt(review.grade), 0);
    return sum / reviews.length;
  }

  private getGradeDistribution(reviews: MusinsaReview[]): string {
    const distribution: { [key: string]: number } = { '5': 0, '4': 0, '3': 0, '2': 0, '1': 0 };
    
    reviews.forEach(review => {
      distribution[review.grade]++;
    });

    const total = reviews.length;
    const bars = Object.entries(distribution)
      .sort((a, b) => parseInt(b[0]) - parseInt(a[0]))
      .map(([grade, count]) => {
        const percentage = total > 0 ? (count / total * 100).toFixed(1) : '0.0';
        const barLength = Math.round(count / total * 20);
        const bar = '█'.repeat(barLength) + '░'.repeat(20 - barLength);
        return `${grade}점: ${bar} ${percentage}% (${count}개)`;
      })
      .join('\n');

    return bars;
  }

  getReviewSummary(reviews: MusinsaReview[]): { 
    positive: string[], 
    negative: string[], 
    mostMentioned: string[] 
  } {
    const positiveReviews = reviews.filter(r => parseInt(r.grade) >= 4);
    const negativeReviews = reviews.filter(r => parseInt(r.grade) <= 2);
    
    // 간단한 키워드 추출 (실제로는 더 정교한 NLP가 필요)
    const keywords: { [key: string]: number } = {};
    
    reviews.forEach(review => {
      const words = review.content.split(/\s+/);
      words.forEach(word => {
        if (word.length > 2) {
          keywords[word] = (keywords[word] || 0) + 1;
        }
      });
    });

    const topKeywords = Object.entries(keywords)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5)
      .map(([word]) => word);

    return {
      positive: positiveReviews.slice(0, 3).map(r => this.truncateContent(r.content, 50)),
      negative: negativeReviews.slice(0, 3).map(r => this.truncateContent(r.content, 50)),
      mostMentioned: topKeywords
    };
  }
}