import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf } from '@angular/common';
import { Gift } from '../../model/gift';
import { GiftService } from '../../services/gift.service';
import { CurrentUserService } from '../../services/current.user.service';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';

import { GiftEditService } from '../../services/gift-edit.service';

import { Subscription } from 'rxjs';

import { FormsModule } from '@angular/forms';


@Component({
  standalone: true,
  selector: 'app-admin-dashboard',
  imports: [
    NgFor,
    NgIf,
    RouterLink,
    RouterOutlet,
    FormsModule
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css',
              '../gifts/gifts.component.css']
})
export class AdminDashboardComponent {

  Title = "Admin Dashboard";

  gifts : Gift[] = [];

  editFlag : boolean | undefined;

  giftEdit : Gift = {
    //the base gift that will be changed eventually so it can be used in the html component
    id: -1, // uninitialized 
    name: '',
    description: '',
    price: 0,
    priority: 'NONE',
    amount_needed: 0
  };

  giftSubscription : Subscription = new Subscription;

  constructor(private giftService : GiftService,
              private currUserService : CurrentUserService,
              private router : Router) {}



  ngOnInit() :void {
    this.checkUser();
    this.getGifts();
    this.editFlag = false;
  }

  checkUser() :void {
    if(!this.currUserService.isAdmin()) {
      this.router.navigate(['login']);
    }
  }

  getGifts()
  {
    this.giftService.getGifts().subscribe((gifts : Gift[]) => this.gifts = gifts);
  }

  addGift(name : string, description : string, passedPrice : string, passedAmount: string, priority: string) : void{
    if(!name){return;}
    if(!description){return;}
    if(!passedPrice){return;}
    if(!passedAmount){return;}

    const price : number = parseFloat(passedPrice);

    const amount_needed : number = parseInt(passedAmount);

    console.log("Passed info: ",name,description,price);

    this.giftService.addGift({ id: 0,
                               name, 
                               description, 
                               price,
                               priority: priority,
                               amount_needed:amount_needed})
    .subscribe(gift => {
      this.gifts.push(gift);
      console.log(gift);
    });
  }

  deleteGift(gift : Gift) {
    this.gifts = this.gifts.filter(g => g!== gift);
    this.giftService.deleteGift(gift.id).subscribe();
  }


  logOut() : void {
    this.currUserService.logOut();
  }

  getDetail(gift : Gift) : void {
    this.editFlag = true;
    this.giftEdit = gift;
  }


  ngOnDestroy() {
    this.giftSubscription.unsubscribe();
  }


  goBack(): void {
    this.editFlag = false;
  }

  saveUpdatedGift(): void {
    if (this.giftEdit.id != -1) {
      this.giftService.updateGift(this.giftEdit)
        .subscribe((returned) => {
          console.log("Updated Gift: ", returned);
        });
    } else {
      console.log("Invalid Gift", this.giftEdit);
      window.alert("Invalid Gift credentials");
    }
    this.editFlag = false;
  }

}
