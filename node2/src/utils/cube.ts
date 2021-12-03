export class Vector3 {
  x: number;
  y: number;
  z: number;
  constructor(x: number, y: number, z: number, offset = 0) {
    this.x = x - offset;
    this.y = y - offset;
    this.z = z - offset;
  }

  add(vector: Vector3): Vector3 {
    return new Vector3(this.x + vector.x, this.y + vector.y, this.z + vector.z);
  }
  multiply(val: number): Vector3 {
    return new Vector3(this.x * val, this.y * val, this.z * val);
  }

  inBounds(min: number, max: number): boolean {
    return this.x >= min && this.y >= min && this.z >= min && this.x < max && this.y < max && this.z < max;
  }

  toString() {
    return `(${this.x}, ${this.y}, ${this.z})`;
  }
}

export class Cube<T> {
  values: T[][][];
  offset: number;

  // constructor(cycles: number, values: T[][][]) {
  constructor(cycles: number, initial: T[][], emptyState: T) {
    // In order to deal with negatives properly use an offset, e.g. if we do 6 cycles use offset of 6 which makes array loop from -6 to + 6
    // Initial input is always based on 0 to positive, but when I store in memory I need -6 to be at index 0, so index 6 (offset) will be the value traditionally at index 0
    this.offset = cycles;

    this.values = [];
    for (let noOffsetZ = -cycles; noOffsetZ < cycles; noOffsetZ++) {
      const z = noOffsetZ + this.offset;
      this.values.push([]); // this array is z
      for (let noOffsetY = -cycles; noOffsetY < cycles; noOffsetY++) {
        const y = noOffsetY + this.offset;
        this.values[z].push([]); // this array is y
        for (let noOffsetX = -cycles; noOffsetX < cycles; noOffsetX++) {
          const x = noOffsetX + this.offset;
          if (
            noOffsetZ === 0 &&
            noOffsetX >= 0 &&
            noOffsetY >= 0 &&
            noOffsetY < initial.length &&
            noOffsetX < initial[noOffsetY].length
          ) {
            // console.log(`Pushing ${initial[noOffsetY][noOffsetX]} to ` + new Vector3(x, y, z, this.offset));
            this.values[z][y][x] = initial[noOffsetY][noOffsetX];
          } else {
            // console.log('Pushing empty state to ' + new Vector3(x, y, z));
            this.values[z][y][x] = emptyState;
          }
        }
      }
    }
  }

  public cycle(active: T, inactive: T): void {
    const positionsToSetActive: Vector3[] = [];
    const positionsToSetInactive: Vector3[] = [];
    for (let noOffsetZ = -this.offset; noOffsetZ < this.offset; noOffsetZ++) {
      for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
        // retVal += this.values[noOffsetZ + this.offset][noOffsetY + this.offset].join(',') + '\n';
        for (let noOffsetX = -this.offset; noOffsetX < this.offset; noOffsetX++) {
          const position = new Vector3(noOffsetX, noOffsetY, noOffsetZ);
          const activeNeighbors = this.getNeighborsOfType(new Vector3(noOffsetX, noOffsetY, noOffsetZ), active);
          switch (this.get(position)) {
            case active: {
              if (activeNeighbors === 2 || activeNeighbors === 3) {
                // remain active
                // positionsToSetActive.push(position);
              } else {
                // this.set(position, inactive);
                positionsToSetInactive.push(position);
              }
            }
            case inactive: {
              if (activeNeighbors === 3) {
                // console.log(`At ${position}, setting to active due to ${activeNeighbors}`);
                // become active
                // this.set(position, active);
                positionsToSetActive.push(position);
              }
            }
            default:
              break;
          }
          // console.log(
          //   'Value: ',
          //   this.values[noOffsetZ + this.offset][noOffsetY + this.offset][noOffsetX + this.offset],
          // );
        }
      }

      // retVal.push(this.values[noOffsetZ]);
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

    for (let noOffsetZ = -this.offset; noOffsetZ < this.offset; noOffsetZ++) {
      for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
        for (let noOffsetX = -this.offset; noOffsetX < this.offset; noOffsetX++) {
          const position = new Vector3(noOffsetX, noOffsetY, noOffsetZ);
          count += this.get(position) === status ? 1 : 0;
        }
      }
    }
    return count;
  }

  getNeighborsOfType(position: Vector3, value: T): number {
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

  getNeighbors(): Vector3[] {
    const neighbors: Vector3[] = [];

    for (let z = -1; z <= 1; z++) {
      for (let y = -1; y <= 1; y++) {
        for (let x = -1; x <= 1; x++) {
          if (z === 0 && y === 0 && x === 0) {
            // skip me
          } else {
            neighbors.push(new Vector3(x, y, z));
          }
        }
      }
    }

    return neighbors;
  }

  get(position: Vector3): T {
    // console.log('Attempting to read position ' + (position.z + this.offset));
    return this.values[position.z + this.offset][position.y + this.offset][position.x + this.offset];
  }

  set(position: Vector3, value: T): void {
    this.values[position.z + this.offset][position.y + this.offset][position.x + this.offset] = value;
  }
  // get(x: number, y: number, z: number): T {
  //   return this.values[y][x][z];
  // }

  // maxX(): number {
  //   return this.values[0].length;
  // }

  // maxY(): number {
  //   return this.values.length;
  // }

  // maxXorY(): number {
  //   return Math.max(this.maxX(), this.maxY());
  // }

  map(cb: (position: Vector3, value: T) => T): T[][][] {
    return this.values.map((depthValue, z) => {
      return depthValue.map((rowValue, y) => {
        return rowValue.map((colValue, x) => {
          return cb(new Vector3(x, y, z), colValue);
        });
      });
    });
  }

  public toString = (status?: T): string => {
    let retVal = '';
    for (let noOffsetZ = -this.offset; noOffsetZ < this.offset; noOffsetZ++) {
      if (this.values[noOffsetZ + this.offset].some((y) => y.some((x) => x === status))) {
        // Print row
        retVal += `z = ${noOffsetZ}\n`;
        for (let noOffsetY = -this.offset; noOffsetY < this.offset; noOffsetY++) {
          retVal += this.values[noOffsetZ + this.offset][noOffsetY + this.offset].join(',') + '\n';
        }
      }

      // retVal.push(this.values[noOffsetZ]);
    }
    //   // // return `(${this.x}, ${this.y}, ${this.z})`;
    return retVal;
  };
}

// export const directions = [
//   new Vector2(1, 0),
//   new Vector2(-1, 0),
//   new Vector2(0, 1),
//   new Vector2(0, -1),
//   new Vector2(1, 1),
//   new Vector2(1, -1),
//   new Vector2(-1, 1),
//   new Vector2(-1, -1),
// ];

// export class Cube<T> {
//   values: T[][][];

//   constructor(values: T[][][]) {
//   // constructor(values: T[][][]) {
//     this.values = values;
//   }

//   getVector(position: Vector3): T {
//     return this.values[position.y][position.x][position.z];
//   }
//   get(x: number, y: number, z: number): T {
//     return this.values[y][x][z];
//   }

//   // maxX(): number {
//   //   return this.values[0].length;
//   // }

//   // maxY(): number {
//   //   return this.values.length;
//   // }

//   // maxXorY(): number {
//   //   return Math.max(this.maxX(), this.maxY());
//   // }

//   map(cb: (position: Vector3, value: T) => T): T[][][] {
//     return this.values.map((depthValue, z) => {
//       return depthValue.map((rowValue, y) => {
//         return rowValue.map((colValue, x) => {
//           return cb(new Vector3(x, y, z), colValue);
//         });
//       });
//     });
//   }
// }

// // export const directions = [
// //   new Vector2(1, 0),
// //   new Vector2(-1, 0),
// //   new Vector2(0, 1),
// //   new Vector2(0, -1),
// //   new Vector2(1, 1),
// //   new Vector2(1, -1),
// //   new Vector2(-1, 1),
// //   new Vector2(-1, -1),
// // ];
