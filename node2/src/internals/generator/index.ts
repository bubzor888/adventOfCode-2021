import '../../utils/env';
import { NodePlopAPI } from 'plop';

import adventOfCode from './aoc';

import axios from 'axios';

export default function (plop: NodePlopAPI) {
  plop.setActionType('fetchUrlData', async function (answers: any, config, plop) {
    try {
      const response = await axios.get(answers.url, {
        headers: {
          cookie: `session=${process.env.session}`,
        },
      });
      answers.responseBody = response.data;
    } catch (e) {
      answers.responseBody = 'error';
      console.error(e);
    }
    return 'good url';
  });
  // plop.setHelper('upperCase2', async (txt) => txt.toUpperCase());
  plop.setGenerator('aoc', adventOfCode);
}
