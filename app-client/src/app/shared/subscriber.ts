import { Subscription } from "rxjs/index";
import { OnDestroy } from "@angular/core";

export abstract class Subscriber implements OnDestroy {

  private subscriptions: Subscription[] = [];

  public ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  protected subscribed(sub: Subscription) {
    this.subscriptions.push(sub);
  }

  protected unsubscribeAll(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
