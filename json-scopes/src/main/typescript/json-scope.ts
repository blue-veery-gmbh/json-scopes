export class JsonScope {
  private _isPositive: boolean;
  private _scope: Set<string> = new Set<string>();

  constructor(isPositive: boolean, scope: string[]) {
    this._isPositive = isPositive;
    for (let objectType of scope) {
      this._scope.add(objectType);
    }
  }

  public isPositive(): boolean {
    return this._isPositive;
  }

  public getScope(): Set<string> {
    return this._scope;
  }

  isInScope(idValue: string) {
    let idComponents = idValue.split("/");
    if (idComponents.length != 2) {
      return true;
    }

    if (this.getScope().has(idComponents[0])) {
      return this.isPositive();
    }else{
      return !this.isPositive();
    }
  }
}
