import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.scss']
})
export class ProfileCardComponent implements OnInit {

  @Input() imageUrl: string;
  @Input() nickName: string;

  constructor() { }

  ngOnInit(): void {
  }

}
