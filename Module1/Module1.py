from builtins import sorted

from ingest import FileIngester


class Module1:

    def __init__(self, file: str) -> None:
        self.file = file
        self.report = {
            "num_of_paragraphs": -1,
            "num_unique_words": -1,
            "num_total_words": -1,
        }

        """
        {
            word: {"col_freq": int, "doc_freq": int}
        }
        """
        self.tf = {}
        self.punct = ['.',  ',', '"', "'", '?', '!', ':', ';', '(', ')', '[', ']', '{', '}', '&', '|', '-']
        self.words = {}

    def run(self) -> None:
        ingester = FileIngester(self.file)
        docs = ingester.read()

        self.report["num_of_paragraphs"] = len(docs)

        clean_docs = self.clean_docs(docs)
        self.process_docs(clean_docs)
        # print(self.tf)
        self.report["num_unique_words"] = len(self.tf.keys())

        print(self.report)

        words = sorted(self.tf.items(), key=self.tf_compare, reverse=True)

        one_doc_words = self.calc_number_of_words_in_one_doc()

        percentage_one_doc_words = one_doc_words/ self.report["num_unique_words"]

        print("Number of words in only 1 document: {}".format(one_doc_words))
        print("Percentage of words in only 1 document: {}".format(percentage_one_doc_words))

        print("Top 100")
        self.print_results(words[0:100])
        print("Top 500th word")
        self.print_results(words[499:500])
        print("Top 1000th word")
        self.print_results(words[999:1000])
        print("Top 5000th word")
        self.print_results(words[4999:5000])

    def tf_compare(self, item: tuple):
        """
        item = (key, dict)
        :param item:
        :return:
        """

        return item[1]["col_freq"]

    def clean_docs(self, docs: list):
        new_docs = []
        for doc in docs:
            new_doc = {
                "id": doc["id"],
                "words": []
            }

            content = doc["content"]  # type: str
            words = [word.lower() for word in content.split() if word.lower() not in self.punct]  # type: list
            words = self.strip_punct(words)  # type: list

            new_doc["words"] = words

            new_docs.append(new_doc)

        return new_docs

    def strip_punct(self, words: list) -> list:
        for punct in self.punct:
            words = [word.strip(punct) for word in words]
        return words

    def process_docs(self, docs):
        for doc in docs:
            doc_set = {}
            for word in doc["words"]:
                self.report["num_total_words"] += 1

                if word in doc_set:
                    doc_set[word] += 1
                else:
                    doc_set[word] = 1

            for word in doc_set:
                if word in self.tf:
                    self.tf[word]["col_freq"] += doc_set[word]
                    self.tf[word]["doc_freq"] += 1
                else:
                    self.tf[word] = {
                        "col_freq": doc_set[word],
                        "doc_freq": 1
                    }

    def print_results(self, list_of_words) -> None:
        """
        list_of_words = [('word', {})]
        :param list_of_words:
        :return:
        """
        for word in list_of_words:
            print("{:>12}: Collection frequency: {:6d}, Document frequency:{:5d}".format(word[0], word[1]["col_freq"], word[1]["doc_freq"]))
        return

    def calc_number_of_words_in_one_doc(self) -> int:
        total = 0
        for word in self.tf:
            if self.tf[word]["doc_freq"] == 1:
                total += 1

        return total


if __name__ == '__main__':
    m = Module1("yelp.txt")
    m.run()

    # m = Module1("headlines.txt")
    # m.run()
