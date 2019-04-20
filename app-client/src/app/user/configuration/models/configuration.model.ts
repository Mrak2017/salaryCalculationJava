import { CheckUtils } from "../../../utils/check-utils";

export class Configuration {
  public id: number;
  public code: string;
  public configValue: string;
  public description: string;

  constructor(data?: any) {
    if (CheckUtils.isExists(data)) {
      this.id = data.id;
      this.code = data.code;
      this.configValue = data.value;
      this.description = data.description;
    }
  }
}
