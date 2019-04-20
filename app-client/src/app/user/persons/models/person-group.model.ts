import { GroupTypeEnum } from "./group-type-enum";
import { CheckUtils } from "../../../utils/check-utils";

export class PersonGroup{
  public id: number;
  public group: GroupTypeEnum;
  public periodStart: Date;
  public periodEnd: Date;

  constructor(data?: any) {
    if (CheckUtils.isExists(data)) {
      this.id = data.id;
      this.periodStart = new Date(data.periodStart);
      this.periodEnd= CheckUtils.isExists(data.periodEnd) ? new Date(data.periodEnd) : null;
      if (CheckUtils.isExists(data.groupType)) {
        this.group = GroupTypeEnum.valueOf(data.groupType);
      }
    }
  }

}
