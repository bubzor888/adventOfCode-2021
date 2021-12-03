import readInput from '../../utils/readInput';
import assert from 'assert';

const rawInput = readInput();
const input = rawInput.split('\n').map(Number);

/* Functions */
const summation = (sum, curr) => sum + curr;
const isGreaterThanPrevious = (val, idx, arr) => (val > arr[idx - 1] ? 1 : 0);
const greaterThanSummation = (values: number[]) => values.map(isGreaterThanPrevious).reduce(summation, 0);

function part1(values: number[]): number {
  return greaterThanSummation(values);
}

function part2(values: number[]): number {
  const theeMeasurements = values.map((v, idx, arr) => v + arr[idx - 1] + arr[idx - 2]).slice(2);
  return greaterThanSummation(theeMeasurements);
}

/* Tests */

assert.strictEqual(part1([199, 200, 208, 210, 200, 207, 240, 269, 260, 263]), 7);

assert.strictEqual(part2([199, 200, 208, 210, 200, 207, 240, 269, 260, 263]), 5);

/* Results */

console.time('Time');
const resultPart1 = part1(input);
const resultPart2 = part2(input);
console.timeEnd('Time');

console.log('Solution to part 1:', resultPart1);
assert.strictEqual(part1(input), 1342);

console.log('Solution to part 2:', resultPart2);
assert.strictEqual(part2(input), 1378);
