import { Component } from '@angular/core';

import { UserService } from "../../services/user.service";
import { CurrentUserService } from '../../services/current.user.service';
import { Router, RouterLink } from '@angular/router';

import { User } from '../../model/user';
import { FormsModule } from '@angular/forms';

import { Location, NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-signup',
  imports: [
    FormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './signup.component.html',
  styleUrl: '../login/login.component.css'
})
export class SignUpComponent {

  constructor(
    private currentUserService: CurrentUserService,
    private userService: UserService,
    private router: Router,
    private location: Location) {}

  userInfo: User = this.currentUserService.getCurrentUser();

  emailDupe : boolean = false;
  invalidInfo : boolean = false;
  invalidPass : boolean = true;
  
  /**
   * Sign up a user by creating a new account
   * @param user 
   */
  signUp(user: User): void {

    this.invalidInfo = false;
    this.emailDupe = false;
    this.invalidPass = false;

    if (this.checkValidInfo(user)) {

      this.userService.isEmailTaken(user.email).then((isTaken: boolean) => {
        if (isTaken) {
          this.emailDupe = true;
        } 
        
        else {
          this.userService.addUser(user).subscribe(
            (userInput : User) => {
              console.log('User data: ', userInput);
              console.log("Success!");
              this.currentUserService.setCurrentUser(userInput);
              this.router.navigate(['']);
          })
        }
      });
    } 
    else if(!this.invalidPass) {
      this.invalidInfo = true;
    }
  }

  /**
   * Check the validity of supplied user information
   * @param user 
   * @returns true if user information is valid
   *          false if not
   */
  checkValidInfo(user: User): boolean {
    return (this.checkValidEmail(user.email) &&
            this.checkValidPassword(user.password) &&
            user.username != '');
  }

  /**
   * Check the validity of an email
   * @param email 
   * @returns true if email is valid
   *          false if not
   */
  checkValidEmail(email: string): boolean {
    // regex that represents an email
    const res = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    if (res.test(email.toLowerCase()))
      return true;
    else {
      return false;
    }
  }

  /**
   * Check the validity of a password
   * @param password 
   * @returns true if password matches these requirements:
   *            - at least 8 characters
   *            - at least 1 uppercase
   *            - at least 1 lowercase
   *            - at least 1 number
   *            - at least 1 special character
   *          false otherwise
   */
  checkValidPassword(password: string): boolean{
    // regex that represents a password with at least 8 characters, one letter, at least 1 number, at least 1 special character
    const res =/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

    if (res.test(password.toLowerCase()))
      return true;
    else {
      this.invalidPass = true;
      return false;
    }
  }

    /**
   * Change view to user's previous route
   */
    goBack(): void {
      this.location.back();
    }
}


