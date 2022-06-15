import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayTwoFactorAuthSecretComponent } from './display-two-factor-auth-secret.component';

describe('DisplayTwoFactorAuthSecretComponent', () => {
  let component: DisplayTwoFactorAuthSecretComponent;
  let fixture: ComponentFixture<DisplayTwoFactorAuthSecretComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DisplayTwoFactorAuthSecretComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayTwoFactorAuthSecretComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
