import { Component } from '@angular/core';

import { CurrentUserService } from '../../services/current.user.service';
import { UserService } from '../../services/user.service';

import { User } from '../../model/user';
import { Router, RouterLink } from '@angular/router';

import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Order } from '../../model/order';
import { Item } from '../../model/item';

@Component({
  standalone: true,
  selector: 'app-account',
  imports: [
    RouterLink,
    NgFor, 
    NgIf,
    FormsModule
  ],
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css',
              '../cart/cart.component.css'
             ]
})
export class AccountComponent {

  orders : Order[] = [];

  totalContributed : number = 0;

  noMoreOrders : boolean = false;

  showOrdersFlag : boolean = false;

  constructor(private currentUserService: CurrentUserService,
              private router: Router,
              private userService : UserService) {}

    currentUser: User = this.currentUserService.getBaseUser(); 

    ngOnInit() {
    if(!this.currentUserService.isUserLoggedIn()) {
      this.router.navigate(["login"]);
      return;
    }
    this.userService.getUserOrders(this.currentUserService.getCurrentUser().email)
    .subscribe((userOrders) => {
      this.currentUserService.setOrders(userOrders);

      this.orders = [];
      for(let i = 0; i < 3; i++) {
        if(userOrders[i] == null) {
          this.noMoreOrders = true;
          break;
        }
        this.orders.push(userOrders[i]);
      }
    });
    this.currentUser = this.currentUserService.getCurrentUser();

    this.userService.getUserTotalContributed(this.currentUser.email)
    .subscribe(total => {this.totalContributed = total});
    }

    saveUser(username : string) {
      this.currentUser.username = username;
      console.log("Updated User: ", this.currentUser);
      this.userService.updateUser(this.currentUser)
      .subscribe(updated => {
        this.currentUserService.setCurrentUser(updated);
      })
    }

    goHome() : void {
      this.router.navigate(['']);
    }

    logOut(): void {
      this.currentUserService.logOut();
    }

    getDetail(id : number) {
      console.log("id: ", id);
      this.router.navigateByUrl(`/detail/${id}`);
    }

    showMoreOrders() : void {

      this.userService.getUserOrders(this.currentUserService.getCurrentUser().email)
      .subscribe((userOrders) =>
        {
          let pastLength = this.orders.length;
          for(let i = pastLength;  i < pastLength + 5;i++) {
            if(userOrders[i] == null) {
              this.noMoreOrders = true;
              break;
            }
            this.orders.push(userOrders[i]);
          }
        });
        return;
    }

    showLessOrders() : void {
      for(let i = this.orders.length; i > 3; i--) {
        this.orders.pop();
      }
      this.noMoreOrders = false;
    }

    showOrders() : void {
      this.showOrdersFlag = true;
    }
}
