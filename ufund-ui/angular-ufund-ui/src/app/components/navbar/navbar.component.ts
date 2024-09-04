import { Component } from '@angular/core';
import { CurrentUserService } from '../../services/current.user.service';
import { GiftSearchComponent } from '../gift-search/gift-search.component';

import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-navbar',
  imports: [
    GiftSearchComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(
    private currentUserService: CurrentUserService,
    private router: Router) {}

  title = 'Navbar';

  /**
   * Update view when "account" button is clicked
   */
  onClickAccount(): void {
    try {
      if (this.currentUserService.isUserLoggedIn())
        this.router.navigate(['/account']); // switch to the user's account page if logged in
      else
        this.router.navigate(['/login']); // switch to log in screen if user not logged in
    } 
    catch (error) {
      console.error(error);
    }
  }

  /**
   * Update view when "cart" button is clicked
   */
  onClickCart(): void {
    try {
      if (this.currentUserService.isUserLoggedIn())
        this.router.navigate(['/cart']); // switch to the user's cart page if logged in
      else
        this.router.navigate(['/login']); // switch to log in screen if user not logged in
    } 
    catch (error) {
      console.error(error);
    }
  }
}
