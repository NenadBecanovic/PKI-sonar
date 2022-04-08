export class ExtensionSettingsDto{
  private extensionsIds: number[];
  private keyUsageIds: number[];
  private extendedKeyUsageIds: number[];
  private publicKey: null;
  private subjectName: null;

  constructor(extensionsIds: number[], keyUsageIds: number[], extendedKeyUsageIds: number[]) {
    this.extensionsIds = extensionsIds;
    this.keyUsageIds = keyUsageIds;
    this.extendedKeyUsageIds = extendedKeyUsageIds;
    this.publicKey = null;
    this.subjectName = null;
  }
}
