import {CompanyBenefit} from "./company";

export interface Feedback {
  id:string;
  userId:string;
  userMartialStatus:string;
  companyId:string;
  location:string;
  userAssociationDate:Date;
  userAssociationYear:string;
  userDesignation:string;
  comment:string;
  rating:number;
  benefits:CompanyBenefit[];
  createdAt:number;

}
