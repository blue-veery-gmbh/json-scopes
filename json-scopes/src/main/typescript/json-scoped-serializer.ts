import {JsonScope} from './json-scope';
import {BaseEntity} from './core-model';

export type ReviverFunction = (key: string, value: any) => any;

export class JsonScopedSerializer {
  public static stringify(object: any, scope: JsonScope, reviver: ReviverFunction = (key: string, value: any) => value): string {
    const serializationContext: { [id: string]: BaseEntity } = {};

    const proxyReplacer = function (key: string, value: Object): any {
      if (!value) {
        return value;
      }
      if (typeof (value) === 'object') {
        const idValue = value['id'];
        if (idValue !== undefined && typeof (idValue) === 'string') {
          if (!scope.isInScope(idValue) || serializationContext[idValue] !== undefined) {
            return {id: idValue};
          } else {
            serializationContext[idValue] = value as BaseEntity;
            return value;
          }
        }
      }

      return reviver(key, value);
    };

    return JSON.stringify(object, proxyReplacer);
  }


  public static parse(text: string, scope: JsonScope = new JsonScope(false, []), reviver: ReviverFunction = (key: string, value: any) => value): any {
    const serializationContext: { [id: string]: BaseEntity } = {};

    const proxyReplacer = function (key: string, value: Object): any {
      if (!value) {
        return value;
      }
      if (typeof (value) === 'object') {
        const idValue = value['id'];
        if (idValue !== undefined && typeof (idValue) === 'string') {
          if (!scope.isInScope(idValue)) {
            return {id: idValue};
          } else {
            const entity = serializationContext[idValue];
            if (entity !== undefined) {
              if (Object.keys(entity).length < Object.keys(value).length) {
                for (const key in value) {
                  entity[key] = value[key];
                }
              }
              return entity;
            }
            serializationContext[idValue] = value as BaseEntity;
            return value;
          }
        }
      }

      return reviver(key, value);
    };
    return JSON.parse(text, proxyReplacer);
  }
}
