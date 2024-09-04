import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-notfound',
  templateUrl: './notfound.component.html',
  styleUrl: './notfound.component.css'
})
export class NotfoundComponent {
  constructor(
    private router: Router) { }
  
  /**
   * Give an option to send the user back home on 404.
   */
  goHome(): void {
    this.router.navigate(['']);
  }
}
