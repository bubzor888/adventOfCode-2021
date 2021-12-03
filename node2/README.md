## Advent of Code

This is my personal playground for [advent of code](https://adventofcode.com/), implemented in typescript.

### One Time Setup

1. Clone this repo `git clone https://github.com/sdg9/AdventOfCode`
2. Install dependencies `npm i`
3. To automate reading of your Advent Of Code puzzle input create a `.env` file at the root of this project with the value `session=<your advent of code session cookie value>`. Then when you generate each day's script it will pull in your associated puzzle input.

### How to Generate a new day

1. Ensure one time setup is complete
2. Run `npm run generate` and follow the prompts, or run `npm run generate <year> <day>`, e.g. `npm run generate 2021 01` for day 1 of 2021.

Now you can work on `src/<year>/<day>/index.ts` and `src/<year>/<day>/input.txt` should be populated with your puzzle input for the day (if input.txt only contains the text `error` make sure your .env file is properly configured).

### Execute your script

1. Run `npm run dev src\<year>\<day>` e.g. `npm run dev src\2021\01` for day 1 of 2021

This will also enter watch mode so that whenever a code change happens the script executes again.

e.g.

```typescript
➜  AdventOfCode git:(main) npm run dev src/2021/01

> node-proxy@1.0.0 dev
> nodemon "src/2021/01"

[nodemon] 2.0.4
[nodemon] to restart at any time, enter `rs`
[nodemon] watching path(s): src/**/*
[nodemon] watching extensions: ts
[nodemon] starting `ts-node src/2021/01`
Time: 0.335ms
Solution to part 1: 1342
Solution to part 2: 1378
[nodemon] clean exit - waiting for changes before restart
```

and then on edit of a file

```typescript
[nodemon] restarting due to changes...
[nodemon] starting `ts-node src/2021/01`
Time: 0.338ms
Solution to part 1: 1342
Solution to part 2: 1378
[nodemon] clean exit - waiting for changes before restart
```

### Preferred IDE

Unsurprisingly I use [VSCode](https://code.visualstudio.com/)

- I recommend the [Prettier - Code Formatter](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) plugin. If it's not formatting on save try "Shift + Alt + F" and typically you'll presented with a prompt on what plugin you want to use to format, choose this prettier plugin. From here on out every time you save a file it should auto format according to your prettier config.
