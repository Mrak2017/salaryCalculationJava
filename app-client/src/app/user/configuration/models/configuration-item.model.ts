import { CheckUtils } from "../../../utils/check-utils";

export class ConfigurationItem {
  public id: number;
  public insertDate: Date;
  public updateDate: Date;
  public code: string;
  public value: string;
  public description: string;

  constructor(data: any) {
    if (CheckUtils.isExists(data)) {
      this.id = data.id;
      this.insertDate = new Date(data.insertDate);
      this.updateDate = new Date(data.updateDate);
      this.code = data.code;
      this.value = data.value;
      this.description = data.description;
    }
  }
}
