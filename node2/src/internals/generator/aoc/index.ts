import * as path from 'path';
import clipboardy from 'clipboardy';
import axios from 'axios';

export default {
  description: 'Add new aoc day',
  prompts: [
    {
      type: 'input',
      name: 'year',
      message: 'What year?',
      validate: (value) => {
        const valueAsInt = Number(value);
        if (Number.isInteger(valueAsInt) && valueAsInt >= 2015 && valueAsInt <= 2021) {
          return true;
        }
        return 'Must be a valid year between 2015 and 2021';
      },
    },
    {
      type: 'input',
      name: 'day',
      message: 'What day?',
      validate: (value) => {
        const valueAsInt = Number(value);
        if (Number.isInteger(valueAsInt) && valueAsInt >= 1 && valueAsInt <= 31) {
          return true;
        }
        return 'Must be a valid day between 1 and 31';
      },
    },
  ],
  actions: (data) => {
    const { year, day } = data;
    const formattedYear = Number(year).toString();
    const integerDay = Number(day);
    const formattedDay = Number(day).toString().padStart(2, '0');

    const actions = [];

    const pathToSrc = path.resolve(__dirname, '../../../');
    const pathToYearANdDay = path.resolve(pathToSrc, formattedYear, formattedDay);

    actions.push({
      type: 'add',
      path: path.resolve(pathToYearANdDay, 'index.ts'),
      templateFile: './aoc/index.ts.hbs',
      abortOnFail: true,
    });

    data.url = `https://adventofcode.com/${formattedYear}/day/${integerDay}/input`;
    actions.push({
      type: 'fetchUrlData',
    });

    actions.push({
      type: 'add',
      path: path.resolve(pathToYearANdDay, 'input.txt'),
      templateFile: './aoc/input.txt.hbs',
      abortOnFail: true,
    });

    const content = `npm run dev src\\${formattedYear}\\${formattedDay}\\`;
    clipboardy.writeSync(content); // to copy command to clipboard as next step is likely to run the generated code
    console.log(content);
    return actions;
  },
};
