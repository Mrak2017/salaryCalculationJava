export class CheckUtils {
  static isExists(value: any): boolean {
    return (value !== null) && (value !== undefined) && (value !== '');
  }
}