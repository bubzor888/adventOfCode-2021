export class Vector4 {
  x: number;
  y: number;
  z: number;
  w: number;
  constructor(x: number, y: number, z: number, w: number, offset = 0) {
    this.x = x - offset;
    this.y = y - offset;
    this.z = z - offset;
    this.w = w - offset;
  }

  add(vector: Vector4): Vector4 {
    return new Vector4(this.x + vector.x, this.y + vector.y, this.z + vector.z, this.w + vector.w);
  }

  inBounds(min: number, max: number): boolean {
    return (
      this.x >= min &&
      this.y >= min &&
      this.z >= min &&
      this.w >= min &&
      this.x < max &&
      this.y < max &&
      this.z < max &&
      this.w < max
    );
  }

  toString(): string {
    return `(${this.x}, ${this.y}, ${this.z}, ${this.w})`;
  }
}

export class Cube4<T> {
  values: T[][][][];
  offset: number;

  // constructor(cycles: number, values: T[][][]) {
  constructor(offset: number, initial: T[][], emptyState: T) {
    // In order to deal with negatives properly use an offset, e.g. if we do 6 cycles use offset of 6 which makes array loop from -6 to + 6
    // Initial input is always based on 0 to positive, but when I store in memory I need -6 to be at index 0, so index 6 (offset) will be the value traditionally at index 0
    this.offset = offset;

    this.values = [];
    for (let noOffsetW = -offset; noOffsetW < offset; noOffsetW++) {
      const w = noOffsetW + this.offset;
      this.values.push([]); // this array is w
      for (let noOffsetZ = -offset; noOffsetZ < offset; noOffsetZ++) {
        const z = noOffsetZ + this.offset;
        this.values[w].push([]); // this array is z
        for (let noOffsetY = -offset; noOffsetY < offset; noOffsetY++) {
          const y = noOffsetY + this.offset;
          this.values[w][z].push([]); // this array is y
          for (let noOffsetX = -offset; noOffsetX < offset; noOffsetX++) {
            const x = noOffsetX + this.offset;
            if (
              noOffsetW === 0 &&
              noOffsetZ === 0 &&
              noOffsetX >= 0 &&
              noOffsetY >= 0 &&
              noOffsetY < initial.length &&
              noOffsetX < initial[noOffsetY].length
            ) {
              this.values[w][z][y][x] = initial[noOffsetY][noOffsetX];
            } else {
              this.values[w][z][y][x] = emptyState;
            }
          }
        }
      }
    }
  }

  public cycle(active: T, inactive: T): void {
    const positionsToSetActive: Vector4[] = [];
    const positionsToSetInactive: Vector4[] = [];
    for (let noOffsetW = -this.offset; noOffsetW < this.offset; noOffsetW++) {
      for (let noOffsetZ = -this.offset; noOffsetZ < this.offset; noOffsetZ++) {
        for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
          for (let noOffsetX = -this.offset; noOffsetX < this.offset; noOffsetX++) {
            const position = new Vector4(noOffsetX, noOffsetY, noOffsetZ, noOffsetW);
            const activeNeighbors = this.getNeighborsOfType(
              new Vector4(noOffsetX, noOffsetY, noOffsetZ, noOffsetW),
              active,
            );
            switch (this.get(position)) {
              case active: {
                if (activeNeighbors === 2 || activeNeighbors === 3) {
                } else {
                  positionsToSetInactive.push(position);
                }
              }
              case inactive: {
                if (activeNeighbors === 3) {
                  positionsToSetActive.push(position);
                }
              }
              default:
                break;
            }
          }
        }
      }
    }
    positionsToSetInactive.forEach((position) => {
      this.set(position, inactive);
    });
    positionsToSetActive.forEach((position) => {
      this.set(position, active);
    });
  }
  getCountOfType(status: T): number {
    let count = 0;

    for (let noOffsetW = -this.offset; noOffsetW < this.offset; noOffsetW++) {
      for (let noOffsetZ = -this.offset; noOffsetZ < this.offset; noOffsetZ++) {
        for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
          for (let noOffsetX = -this.offset; noOffsetX < this.offset; noOffsetX++) {
            const position = new Vector4(noOffsetX, noOffsetY, noOffsetZ, noOffsetW);
            count += this.get(position) === status ? 1 : 0;
          }
        }
      }
    }
    return count;
  }

  getNeighborsOfType(position: Vector4, value: T): number {
    return this.getNeighbors().reduce((acc, neighbor) => {
      const newPostion = neighbor.add(position);
      if (!newPostion.inBounds(-this.offset, this.offset)) {
        return acc;
      } else {
        const match = this.get(neighbor.add(position)) === value ? 1 : 0;
        return acc + match;
      }
    }, 0);
  }

  getNeighbors(): Vector4[] {
    const neighbors: Vector4[] = [];

    for (let w = -1; w <= 1; w++) {
      for (let z = -1; z <= 1; z++) {
        for (let y = -1; y <= 1; y++) {
          for (let x = -1; x <= 1; x++) {
            if (z === 0 && y === 0 && x === 0 && w === 0) {
              // skip me
            } else {
              neighbors.push(new Vector4(x, y, z, w));
            }
          }
        }
      }
    }

    return neighbors;
  }

  get(position: Vector4): T {
    return this.values[position.w + this.offset][position.z + this.offset][position.y + this.offset][
      position.x + this.offset
    ];
  }

  set(position: Vector4, value: T): void {
    this.values[position.w + this.offset][position.z + this.offset][position.y + this.offset][
      position.x + this.offset
    ] = value;
  }

  public toString = (range = this.offset): string => {
    let retVal = '';
    // For testing just print out a few rows
    // for (let noOffsetW = -this.offset; noOffsetW < this.offset; noOffsetW++) {
    for (let noOffsetW = -range; noOffsetW < range; noOffsetW++) {
      retVal += '\n';
      for (let noOffsetZ = -range; noOffsetZ < range; noOffsetZ++) {
        retVal += `z = ${noOffsetZ}, w = ${noOffsetW}\n`;
        for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
          retVal +=
            this.values[noOffsetW + this.offset][noOffsetZ + this.offset][noOffsetY + this.offset].join(',') + '\n';
        }
      }
    }
    return retVal;
  };
}
