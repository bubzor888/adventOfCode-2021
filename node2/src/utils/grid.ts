import { allowedNodeEnvironmentFlags } from 'process';

export class Vector2 {
  x: number;
  y: number;
  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }

  add(vector: Vector2): Vector2 {
    return new Vector2(this.x + vector.x, this.y + vector.y);
  }
  multiply(val: number): Vector2 {
    return new Vector2(this.x * val, this.y * val);
  }

  inBounds(maxX: number, maxY: number): boolean {
    return this.x >= 0 && this.y >= 0 && this.x < maxX && this.y < maxY;
  }

  toString() {
    return `(x: ${this.x}, y: ${this.y})`;
  }
}

export enum Direction {
  North,
  East,
  South,
  West,
}

export class Grid<T> {
  values: T[][];

  constructor(values: T[][]) {
    this.values = values;
  }

  getVector(position: Vector2): T {
    return this.values[position.y][position.x];
  }
  get(x: number, y: number): T {
    return this.values[y][x];
  }

  maxX(): number {
    return this.values[0].length;
  }

  maxY(): number {
    return this.values.length;
  }

  maxXorY(): number {
    return Math.max(this.maxX(), this.maxY());
  }

  map(cb: (position: Vector2, value: T) => T): T[][] {
    return this.values.map((rowValue, y) => {
      return rowValue.map((colValue, x) => {
        return cb(new Vector2(x, y), colValue);
      });
    });
  }

  rotateCounterClockwise() {
    const xLength = this.maxX();
    return this.map((position, value) => {
      return this.get(xLength - 1 - position.y, position.x);
    });
  }

  // Rotate clockwise
  rotate() {
    const xLength = this.maxX();
    const yLength = this.maxY();
    // this.values = this.map((position, value) => {
    //   // return this.get(xLength - 1 - position.y, position.x);
    //   return this.get(position.y, yLength - 1 - position.x);
    // });
    return new Grid(
      this.map((position, value) => {
        return this.get(position.y, yLength - 1 - position.x);
      }),
    );
  }
  flipOnYPlane() {
    const xLength = this.maxX();
    const yLength = this.maxY();
    // this.values = this.map((position, value) => {
    //   // return this.get(xLength - 1 - position.y, position.x);
    //   return this.get(position.y, yLength - 1 - position.x);
    // });
    return new Grid(
      this.map((position, value) => {
        return this.get(xLength - 1 - position.x, position.y);
      }),
    );
  }

  // alignsCurry(direction: Direction) {
  //   return function (grid: Grid<T>): boolean {
  //     return this.aligns(direction, grid);
  //   };
  // }

  alignsCurry(direction: Direction) {
    return function (grid: Grid<T>): boolean {
      return this.aligns(direction, grid);
    };
  }

  // aligns(grid: Grid<T>, direction: Direction) {
  alignsXWardWith(direction: Direction, grid: Grid<T>): boolean {
    switch (direction) {
      case Direction.North: {
        return this.values[0].join('') === grid.values[grid.maxY() - 1].join('');
      }
      case Direction.South: {
        return grid.values[0].join('') === this.values[this.maxY() - 1].join('');
      }
      case Direction.East: {
        return grid.values.map((y) => y[0]).join('') === this.values.map((y) => y[this.maxX() - 1]).join('');
      }
      case Direction.West: {
        // console.log('does: ', );
        return this.values.map((y) => y[0]).join('') === grid.values.map((y) => y[grid.maxX() - 1]).join('');
      }
      default:
        return false;
    }
  }
}

export const directions = [
  new Vector2(1, 0),
  new Vector2(-1, 0),
  new Vector2(0, 1),
  new Vector2(0, -1),
  new Vector2(1, 1),
  new Vector2(1, -1),
  new Vector2(-1, 1),
  new Vector2(-1, -1),
];
