import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoFactorAuthLoginComponent } from './two-factor-auth-login.component';

describe('TwoFactorAuthLoginComponent', () => {
  let component: TwoFactorAuthLoginComponent;
  let fixture: ComponentFixture<TwoFactorAuthLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TwoFactorAuthLoginComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TwoFactorAuthLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
