const assert = require('assert/strict')
const path = require('path')
const { read, position } = require('promise-path')
const fromHere = position(__dirname)
const report = (...messages) => console.log(`[${require(fromHere('../../package.json')).logName} / ${__dirname.split(path.sep).pop()}]`, ...messages)

async function run () {
  const exampleInput = (await read(fromHere('exampleInput.txt'), 'utf8')).trim().split('\n')
  report('Example Input:', exampleInput)
  assert.strictEqual(150, await followInstructions(exampleInput))

  const input = (await read(fromHere('input.txt'), 'utf8')).trim().split('\n')
  report('Input:', input)
  await solveForFirstStar(input)

  assert.strictEqual(900, await followInstructions2(exampleInput))
  await solveForSecondStar(input)
}

async function followInstructions (inputList) {
  let x = 0
  let y = 0
  inputList.forEach(input => {
    const command = input.split(' ')
    switch (command[0]) {
      case 'forward':
        x += parseInt(command[1])
        break
      case 'up':
        y -= parseInt(command[1])
        break
      case 'down':
        y += parseInt(command[1])
        break
      default:
        console.log(`Invalid command: ${command[0]}`)
    }
  })

  return x * y
}

async function solveForFirstStar (input) {
  report('Solution 1:', await followInstructions(input))
}

async function followInstructions2 (inputList) {
  let x = 0
  let y = 0
  let aim = 0
  inputList.forEach(input => {
    const command = input.split(' ')
    const amount = parseInt(command[1])
    switch (command[0]) {
      case 'forward':
        x += amount
        y += aim * amount
        break
      case 'up':
        aim -= amount
        break
      case 'down':
        aim += amount
        break
      default:
        console.log(`Invalid command: ${command[0]}`)
    }
  })

  return x * y
}

async function solveForSecondStar (input) {
  report('Solution 2:', await followInstructions2(input))
}

run()
