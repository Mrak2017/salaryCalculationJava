import { CheckUtils } from "../../../utils/check-utils";

export class OrgStructureItem {
  public personId: number;
  public parentId: number;
  public fullName: string;
  public children?: OrgStructureItem[];

  constructor(parentId:number, data?: any) {
    if (CheckUtils.isExists(data)) {
      this.personId = data.personId;
      this.parentId = parentId;
      const middleName = data.middleName ? ' ' + data.middleName : '';
      this.fullName = data.lastName + ' ' + data.firstName + middleName;
      if (CheckUtils.isExists(data.children)) {
        this.children = data.children.map(val => new OrgStructureItem(data.personId, val));
      }
    }
  }
}