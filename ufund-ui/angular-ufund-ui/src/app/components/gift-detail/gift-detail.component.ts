import { Component, NgModule, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location, NgIf } from '@angular/common';
import { Router } from '@angular/router';

import { Gift } from '../../model/gift';
import { GiftService } from '../../services/gift.service';
import { FormsModule } from '@angular/forms';

import { CurrentUserService } from '../../services/current.user.service';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';

@Component({
  standalone: true,
  selector: 'app-gift-detail',
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './gift-detail.component.html',
  styleUrls: ['./gift-detail.component.css']
})
export class GiftDetailComponent implements OnInit {
  gift: Gift | undefined;

  isUser : boolean = false;

  constructor(
    private route: ActivatedRoute,
    private giftService: GiftService,
    private location: Location,
    private router: Router,
    private currUserService : CurrentUserService,
    private userService : UserService
  ) {}

  ngOnInit(): void {
    this.isUser = this.isUserLoggedIn();
    this.getGift();
  }

  getGift(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.giftService.getGift(id)
      .subscribe(gift => {
        if (gift) {
          this.gift = gift;
        } else {
          this.router.navigate(['/404']); // /404 isn't actually a page, so in turn it goes to a 404 anyway. I guess it works.
        }
      })
  }

  /**
   * Change view to user's previous route
   */
  goBack(): void {
    this.location.back();
  }

  isUserLoggedIn() : boolean{
    return this.currUserService.isUserLoggedIn() &&
    !this.currUserService.isAdmin();
  }

  /**
   * Add the selected gift to the cart
   * @param gift 
   */
  addToCart(gift: Gift, passedAmount : string = "1"): void {
    if(this.currUserService.isUserLoggedIn()) {
      console.log("Gift info: ", gift);

      const amount : number = parseInt(passedAmount);

      this.userService.addItemToCart(this.currUserService.getCurrentUser().email,
                                     this.giftService.createItemFromGiftId(gift.id, gift.name, amount)).subscribe(
                                        (user: User) => {this.currUserService.setCurrentUser(user);
                                                 console.log('Success!', user.id)});
      
      console.log(this.currUserService.getCurrentUser().cart.length);
    } 
    
    else 
      this.router.navigate(['/login']);
  }
}
