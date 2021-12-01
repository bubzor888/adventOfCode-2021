const assert = require('assert/strict')
const path = require('path')
const { read, position } = require('promise-path')
const fromHere = position(__dirname)
const report = (...messages) => console.log(`[${require(fromHere('../../package.json')).logName} / ${__dirname.split(path.sep).pop()}]`, ...messages)

async function run () {
  const exampleInput = ['199', '200', '208', '210', '200', '207', '240', '269', '260', '263'].map(Number)
  const input = (await read(fromHere('input.txt'), 'utf8')).trim().split('\n').map(Number)

  assert.strictEqual(7, await countDepthIncreases(exampleInput))
  await solveForFirstStar(input)

  assert.strictEqual(5, await countDepthIncreaseBy3(exampleInput))
  await solveForSecondStar(input)
}

async function countDepthIncreases (input) {
  let count = 0
  for (let i = 1; i < input.length; i++) {
    if (input[i] > input[i - 1]) {
      count++
    }
  }
  return count
}

async function countDepthIncreaseBy3 (input) {
  let count = 0
  for (let i = 3; i < input.length; i++) {
    const prev = input[i - 3] + input[i - 2] + input[i - 1]
    const current = input[i - 2] + input[i - 1] + input[i]
    if (current > prev) {
      count++
    }
  }
  return count
}

async function solveForFirstStar (input) {
  report('Input:', input)
  report('Solution 1:', await countDepthIncreases(input))
}

async function solveForSecondStar (input) {
  report('Solution 2:', await countDepthIncreaseBy3(input))
}

run()
