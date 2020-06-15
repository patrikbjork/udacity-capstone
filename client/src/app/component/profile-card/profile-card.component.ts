import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.scss']
})
export class ProfileCardComponent implements OnInit {

  @Input() imageUrl: string;
  @Input() name: string;
  @Input() userId: string;
  @Input() online: boolean;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  startChatting(userId: string) {
    this.router.navigate(['chat', userId]);
  }
}
