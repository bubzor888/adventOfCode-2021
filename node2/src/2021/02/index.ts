import readInput from '../../utils/readInput';
import assert from 'assert';

const rawInput = readInput();
const input = rawInput.split('\n').map(String);

/* Types */
type Command = {
  forward: (qty: number) => void;
  up: (qty: number) => void;
  down: (qty: number) => void;
};

/* Functions */
const processCommand = (values: string[], commands: Command) => {
  values.forEach((value) => {
    const [command, qtyString] = value.split(' ');
    const qty = parseInt(qtyString, 10);
    commands[command]?.(qty);
  });
};

function part1(values: string[]): number {
  let horizontal = 0;
  let depth = 0;

  processCommand(values, {
    forward: (qty) => (horizontal += qty),
    up: (qty) => (depth -= qty),
    down: (qty) => (depth += qty),
  });
  return horizontal * depth;
}

function part2(values: string[]): number {
  let horizontal = 0;
  let depth = 0;
  let aim = 0;

  processCommand(values, {
    forward: (qty) => {
      horizontal += qty;
      depth += aim * qty;
    },
    up: (qty) => (aim -= qty),
    down: (qty) => (aim += qty),
  });
  return horizontal * depth;
}

/* Tests */

assert.strictEqual(part1(['forward 5', 'down 5', 'forward 8', 'up 3', 'down 8', 'forward 2']), 150);
assert.strictEqual(part1(input), 1693300);

assert.strictEqual(part2(['forward 5', 'down 5', 'forward 8', 'up 3', 'down 8', 'forward 2']), 900);
assert.strictEqual(part2(input), 1857958050);

/* Results */

console.time('Time');
const resultPart1 = part1(input);
const resultPart2 = part2(input);
console.timeEnd('Time');

console.log('Solution to part 1:', resultPart1);
console.log('Solution to part 2:', resultPart2);
