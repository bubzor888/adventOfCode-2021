import { astar, Graph } from '../astar';

// Graph of weights 0 = wall, otherwise weight >= 1
const graph = new Graph([
  [1, 0, 1, 1, 1],
  [1, 1, 1, 0, 1],
  [0, 0, 1, 0, 1],
]);

const start = graph.grid[0][0];
const end = graph.grid[2][4];
const result = astar.search(graph, start, end);

// console.log('start: ', start);
console.log(
  'result: ',
  result.map((i) => `(${i.x}, ${i.y})`),
);

// console.log(graph.toString());
// console.log('Total nodes: ' + graph.nodes.length);
// console.log('Nodes: ' + graph.nodes.map((i) => `(${i.x}, ${i.y}: ${i.weight})`));
