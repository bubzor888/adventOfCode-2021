const assert = require('assert/strict')
const path = require('path')
const { read, position } = require('promise-path')
const fromHere = position(__dirname)
const report = (...messages) => console.log(`[${require(fromHere('../../package.json')).logName} / ${__dirname.split(path.sep).pop()}]`, ...messages)

async function run () {
  const exampleInput = []
  const input = (await read(fromHere('input.txt'), 'utf8')).trim().split('\n').map(Number)
  report('Input:', input)

  assert.strictEqual(0, await placeholder(exampleInput))
  await solveForFirstStar(input)

  assert.strictEqual(0, await placeholder(exampleInput))
  await solveForSecondStar(input)
}

async function placeholder(exampleInput) {
  return 0
}

async function solveForFirstStar (input) {
  report('Solution 1:', placeholder(input))
}

async function solveForSecondStar (input) {
  report('Solution 2:', placeholder(input))
}

run()
