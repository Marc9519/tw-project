import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { fromEvent, Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
  clientWidth?: number;
  clientHeight?: number;
  pointerLocationX?: number;
  pointerLocationY?: number;

  resizeSubscription?: Subscription;

  /**
   *
   */
  ngOnInit(): void {
    this.clientWidth = document.documentElement.clientWidth;
    this.clientHeight = document.documentElement.clientHeight;
    // Create Observable and subscribe to resize event
    const resizeObservable = fromEvent(window, 'resize');
    this.resizeSubscription = resizeObservable.subscribe((event) => {
      this.clientWidth = document.documentElement.clientWidth;
      this.clientHeight = document.documentElement.clientHeight;
    });

    fromEvent(document, 'mousemove').subscribe((event: any) => {     
      this.pointerLocationX= event.clientX;
      this.pointerLocationY= event.clientY;
    });
  }

  ngOnDestroy(): void {
    this.resizeSubscription?.unsubscribe();
  }

}
