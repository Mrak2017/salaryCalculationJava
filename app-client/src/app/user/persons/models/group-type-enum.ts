import { Enum, EnumType } from "ts-jenum";

@Enum("code")
export class GroupTypeEnum extends EnumType<GroupTypeEnum>() {
  static readonly EMPLOYEE = new GroupTypeEnum("Employee", "Сотрудник");
  static readonly MANAGER = new GroupTypeEnum("Manager", "Менеджер");
  static readonly SALESMAN = new GroupTypeEnum("Salesman", "Продажник");

  private constructor(public code: string, public name: string) {
    super();
  }
}
