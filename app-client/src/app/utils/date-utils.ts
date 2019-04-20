import * as moment from 'moment';
import { Moment } from "moment";

export class DateUtils {

  static getMoment(value: any): Moment {
    return moment(value).locale('ru');
  }

  static formatDateOnly(value: Date): String {
    return this.getMoment(value).format("DD.MM.YYYY");
  }

  static formatNoTimeZoneDayStart(value: Date): String {
    return this.getMoment(value).startOf('day').format("YYYY-MM-DDTHH:mm:ss");
  }

  static formatNoTimeZoneDayEnd(value: Date): String {
    return this.getMoment(value).endOf('day').format("YYYY-MM-DDTHH:mm:ss");
  }
}