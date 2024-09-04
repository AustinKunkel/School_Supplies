import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import { debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';

import { Gift } from '../../model/gift';
import { GiftService } from '../../services/gift.service';
import { AsyncPipe, NgFor, NgIf, NgClass } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CurrentUserService } from '../../services/current.user.service';
import { UserService } from '../../services/user.service';

@Component({
  standalone: true,
  selector: 'gift-search',
  imports: [
    AsyncPipe,
    NgFor,
    RouterLink,
    NgIf,
    NgClass
  ],
  templateUrl: './gift-search.component.html',
  styleUrl: './gift-search.component.css'
})
export class GiftSearchComponent implements OnInit {

  hasGifts : boolean = false;

  gifts$!: Observable<Gift[]>;

  private searchTerms = new Subject<string>();

  constructor(private currUserService: CurrentUserService,
              private giftService: GiftService,
              private userService: UserService,
              private router: Router,
              ) {}

  ngOnInit(): void {
    this.gifts$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.giftService.searchGifts(term)
      .pipe(tap((gifts: Gift[]) => {
        this.hasGifts = gifts.length > 0;
      }))),
    );
  }

  // push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  getDetail(id : number) {
    console.log("id: ", id);
    this.router.navigateByUrl(`/detail/${id}`);
  }

}
