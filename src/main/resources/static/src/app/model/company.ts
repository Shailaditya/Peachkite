export interface Company {
    id:string;
    name:string;
    description:string;
    logo:string;
    offices:Address[];
    size:number;
    industry:string;
    aboutUs:string;
    aboutUsImage:string;
    isVerified:boolean;
    rating:number;
    benefits:CompanyBenefit[];
    feedbackCount:number;
    commentCount:number;
    searchCount:number;
    benefitDescription:string;
    adminUserId:string;
    categoryRatings:any;
    locationHq:string;
    employeeRange:string;
}

export interface Address{
    addressLine1:string;
    addressLine2:string;
    zipcode:string;
    city:string;
    state:string;
    country:string;
}

export interface CompanyBenefit{
    id:string;
    label:string;
    rating:number;
    type:number;
    category:number;
    isSelected:boolean;
}
