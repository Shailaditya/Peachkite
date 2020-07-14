import { PeachkitePage } from './app.po';

describe('peachkite App', function() {
  let page: PeachkitePage;

  beforeEach(() => {
    page = new PeachkitePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
