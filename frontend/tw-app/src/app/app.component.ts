import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { fromEvent, Subscription  } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
  clientWidth?: number;
  clientHeight?: number;
  resizeSubscription? : Subscription;

  /**
   *
   */
  ngOnInit(): void {
    const resizeObservable = fromEvent(window, 'resize');
    this.resizeSubscription = resizeObservable.subscribe((event) => {
      this.clientWidth = document.documentElement.clientWidth;
      this.clientHeight = document.documentElement.clientHeight;
    });
  }

  ngOnDestroy(): void {
    this.resizeSubscription?.unsubscribe();
  }
}
