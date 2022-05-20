export class ForgottenPasswordDto {
  private newPassword: string;
  private newPasswordRetyped: string;

  constructor(newPassword: string, newPasswordRetyped: string) {
    this.newPassword = newPassword;
    this.newPasswordRetyped = newPasswordRetyped;
  }

}