import { GroupTypeEnum } from "./group-type-enum";
import { CheckUtils } from "../../../utils/check-utils";

export class PersonItem {
  public id: number;
  public login: string;
  public fullName: string;
  public startDate: Date;
  public currentGroup: GroupTypeEnum;
  public baseSalaryPart: number;
  public currentSalary: number;

  constructor(data: any) {
    if (CheckUtils.isExists(data)) {
      this.id = data.id;
      this.login = data.login;
      const middleName = data.middleName ? ' ' + data.middleName : '';
      this.fullName = data.lastName + ' ' + data.firstName + middleName;
      this.startDate = new Date(data.startDate);
      if (CheckUtils.isExists(data.currentGroup)) {
        this.currentGroup = GroupTypeEnum.valueOf(data.currentGroup);
      }
      this.baseSalaryPart = data.baseSalaryPart;
      this.currentSalary = data.currentSalary;
    }
  }

}
