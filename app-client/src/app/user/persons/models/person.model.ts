import { GroupTypeEnum } from "./group-type-enum";
import { CheckUtils } from "../../../utils/check-utils";
import { PersonGroup } from "./person-group.model";
import { OrgStructureItem } from "./org-structure-item";

export class Person {
  public static readonly NAME_MAX_LENGTH = 100;

  public id: number;
  public login: string;
  public password: string;
  public firstName: string;
  public middleName: string;
  public lastName: string;
  public fullNameDots: string;
  public startDate: Date;
  public endDate: Date;
  public currentGroup: GroupTypeEnum;
  public baseSalaryPart: number;
  public currentSalary: number;
  public currentChief: Person;
  public groups: PersonGroup[];
  public children: OrgStructureItem;

  constructor(data: any) {
    if (CheckUtils.isExists(data)) {
      this.id = data.id;
      this.login = data.login;
      this.password = data.password;
      this.firstName = data.firstName;
      this.middleName = data.middleName;
      this.lastName = data.lastName;
      const middleName = data.middleName ? ' ' + data.middleName.substring(0,1) + '.' : '';
      this.fullNameDots = data.lastName + ' ' + data.firstName.substring(0,1) + '.' + middleName;
      this.startDate = new Date(data.startDate);
      this.endDate = CheckUtils.isExists(data.endDate) ? new Date(data.endDate) : null;
      if (CheckUtils.isExists(data.currentGroup)) {
        this.currentGroup = GroupTypeEnum.valueOf(data.currentGroup);
      }
      this.baseSalaryPart = data.baseSalaryPart;
      this.currentSalary = data.currentSalary;
      if(CheckUtils.isExists(data.currentChief)) {
        this.currentChief = new Person(data.currentChief);
      }

      if (CheckUtils.isExists(data.groups)) {
        this.groups = data.groups.map(val => new PersonGroup(val));
      }

      if (CheckUtils.isExists(data.children)) {
        this.children = new OrgStructureItem(0, data.children);
      }
    }
  }
}
