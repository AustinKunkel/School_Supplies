import { Component, OnInit } from '@angular/core';

import { Gift } from '../../model/gift';
import { GiftService } from '../../services/gift.service';
import { UserService } from '../../services/user.service';
import { CurrentUserService } from '../../services/current.user.service';
import { Router, RouterLink } from '@angular/router';
import { User } from '../../model/user';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-gifts',
  imports: [
    NgFor, RouterLink, CommonModule, FormsModule
  ],
  templateUrl: './gifts.component.html',
  styleUrls: ['./gifts.component.css']
})
export class GiftsComponent implements OnInit {
  gifts: Gift[] = [];
  noGiftsReturned = false; // if no gifts are returned
  sortBy: string = 'none';

  // Gifts are sorted on frontend. Priority is represented again here.
  private priorityOrder: { [priority: string]: number } = {
    'NONE' : 0,
    'LOW' : 1,
    'MID' : 2,
    'HIGH' : 3,
    'SEVERE' : 4
  };

  constructor(private giftService: GiftService,
              private userService : UserService,
              private currUserService : CurrentUserService,
              private router : Router) { }

  ngOnInit(): void {
    this.getGifts();
  }

    getGifts(): void {
      this.giftService.getGifts()
      .subscribe(gifts => {
        this.gifts = gifts;
        this.noGiftsReturned = !gifts || gifts.length === 0;
      })
    }

  /**
   * Sort the gifts by the priorty specified in the drop down menu.
   */
    sortGifts(): void {
      if (this.sortBy === 'ascending') {
        this.gifts.sort((a, b) => this.priorityOrder[a.priority] - this.priorityOrder[b.priority]);
      } else if (this.sortBy === 'descending') {
        this.gifts.sort((a, b) => this.priorityOrder[b.priority] - this.priorityOrder[a.priority]);
      }
    }

  /**
   * Add the selected gift to the cart
   * @param gift 
   */
  addToCart(gift: Gift, passedAmount : string): void {

    passedAmount = passedAmount == "" ? "1" : passedAmount;

    if(this.currUserService.isUserLoggedIn()) {
      console.log("Gift info: ", gift);

      let amount = parseInt(passedAmount);
  
      if(this.isAmountOver(gift, amount)) {
        amount = gift.amount_needed;
      }

      this.userService.addItemToCart(this.currUserService.getCurrentUser().email,
                                     this.giftService.createItemFromGiftId(gift.id, gift.name, amount))
                                     .subscribe((user: User) => {
                                      this.currUserService.setCurrentUser(user);
                                      console.log('Success!', user.id)});
      
      console.log(this.currUserService.getCurrentUser().cart.length);
    } 
    
    else 
      this.router.navigate(['/login']);
  }

  isAmountOver(gift: Gift, amount: number) {
    return gift.amount_needed < amount;
  }
}
