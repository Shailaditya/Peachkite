export interface Notification {
  id:string;
  companyId:string;
  from:string;
  detail:string;
  response:string;
  status:string;
  type:string;
  createdAt:Date;
  repliedDate:Date;
  feedbackId:string;
  queryId:string;
}
