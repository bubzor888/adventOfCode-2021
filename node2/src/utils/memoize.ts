/**
 * Can cache results for pure functions
 *
 * @param f to memoize
 */
export const memoize = <T extends (...args: never[]) => ReturnType<T>>(
  f: T,
  cache = new Map<string, ReturnType<T>>(),
): T =>
  ((...args: never[]) => {
    const hash = JSON.stringify(args);
    const cached = cache.get(hash);
    if (cached) {
      return cached;
    } else {
      const result = f(...args);
      cache.set(hash, result);
      return result;
    }
  }) as T;
