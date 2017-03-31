// https://github.com/wvengen/d3-wordcloud
d3.text("data.min.tsv", function(error, text) {
    if (error) throw error;

    var frequency_list = d3.tsv.parseRows(text, function(d, i) {
        return {
          text: d[0],
          size: +d[1]
        };
    });

    d3.wordcloud()
        .size([1400, 600])
        .selector('#wordcloud')
        .words(frequency_list)
        .start();
});
