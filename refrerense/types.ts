export interface MusinsaProduct {
  goodsNo: number;
  goodsName: string;
  goodsLinkUrl: string;
  thumbnail: string;
  displayGenderText: string;
  isSoldOut: boolean;
  normalPrice: number;
  price: number;
  saleRate: number;
  brand: string;
  brandName: string;
  brandLinkUrl: string;
  reviewCount: number;
  reviewScore: number;
  isOptionVisible: boolean;
  isAd: boolean;
  plusDeliveryGuideText: string;
  infoLabelList: InfoLabel[];
  imageLabelList: ImageLabel[];
  clickTrackers: string[];
  impressionTrackers: string[];
  snap: any;
  score: number;
  isPlusDelivery: boolean;
}

export interface InfoLabel {
  code: string;
  title: string;
  color: string;
}

export interface ImageLabel {
  code: string;
  title: string;
  color: string;
}

export interface MusinsaApiResponse {
  data: {
    list: MusinsaProduct[];
    pagination: {
      page: number;
      size: number;
      totalCount: number;
      totalPages: number;
      hasNext: boolean;
    };
    dsBucketProduct: string;
  };
  meta: {
    result: string;
  };
}

export interface SearchParams {
  keyword?: string;
  gf?: string;
  sortCode?: string;
  page?: number;
  size?: number;
  caller?: string;
}

// Review API Types
export interface ReviewGoods {
  goodsNo: number;
  goodsSubNo: number;
  goodsName: string;
  goodsImageFile: string;
  goodsImageExtension: string;
  goodsOptionKindCode: string;
  brandName: string;
  brandEnglishName: string;
  brand: string;
  brandBestYn: string;
  brandConcatenation: string;
  goodsCreateDate: string;
  goodsImageIdx: number;
  saleStatCode: string;
  saleStatLabel: string | null;
  goodsSex: number;
  goodsSexClassification: string | null;
  showSoldOut: boolean;
}

export interface ReviewUserProfile {
  userNickName: string;
  userLevel: number;
  userOutYn: string;
  userStaffYn: string;
  reviewSex: string;
  userWeight: number;
  userHeight: number;
  userSkinInfo: string | null;
  skinType: string | null;
  skinTone: string | null;
  skinWorry: string | null;
}

export interface ReviewImage {
  altText: string;
  imageUrl: string;
}

export interface MusinsaReview {
  no: number;
  type: string;
  typeName: string;
  subType: string | null;
  content: string;
  commentCount: number;
  grade: string;
  goods: ReviewGoods;
  userImageFile: string;
  goodsOption: string;
  commentReplyCount: number;
  userStaffYn: string;
  images: ReviewImage[] | null;
  likeCount: number;
  userReactionType: string | null;
  createDate: string;
  goodsThumbnailImageUrl: string;
  userId: string | null;
  encryptedUserId: string;
  userProfileInfo: ReviewUserProfile;
  orderOptionNo: number;
  channelSource: string;
  channelSourceName: string;
  channelActivityId: string;
  relatedNo: number;
  isFirstReview: number;
  reviewProfileTypeEnum: string;
  specialtyCodes: string | null;
  reviewerWeeklyRanking: number | null;
  reviewerMonthlyRanking: number | null;
  showUserProfile: boolean;
}

export interface ReviewPagination {
  page: number;
  totalPages: number;
  startPage: number;
  lastPage: number;
}

export interface MusinsaReviewApiResponse {
  data: {
    list: MusinsaReview[];
    total: number;
    page: ReviewPagination;
  };
  meta: {
    result: string;
    errorCode: string | null;
    message: string | null;
  };
}

export interface ReviewSearchParams {
  page?: number;
  pageSize?: number;
  goodsNo: number;
  sort?: 'up_cnt_desc' | 'date_desc' | 'grade_desc' | 'grade_asc';
  selectedSimilarNo?: number;
  myFilter?: boolean;
  hasPhoto?: boolean;
  isExperience?: boolean;
}