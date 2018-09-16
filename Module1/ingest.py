import os


class FileIngester:

    def __init__(self, file_location: str):
        if os.path.isfile(file_location):
            self.file_location = file_location
        else:
            self.file_location = None

        self.documents = []

    def read(self) -> list:
        """
        document = {
            id=number,
            content=""
        }
        :return:
        """
        if self.file_location is None:
            return {}

        lines = []
        with open(self.file_location, 'r') as f:
            document = self.new_document()
            for line in f:
                line = line.strip()

                if line == "":
                    continue

                if "<P ID=" in line:
                    line = line.replace("<P ID=", "")
                    line = line.replace(">", "")
                    id=-1
                    try:
                        id = int(line)
                        document["id"] = id
                    except:
                        pass

                elif "</P>" in line:
                    self.documents.append(document)
                    document = self.new_document()

                else:
                    document["content"] += line

        return self.documents

    def new_document(self) -> dict:
        return {"id": -1, "content": ""}


if __name__ == '__main__':
    x = FileIngester("yelp.txt")
    docs = x.read()
    print(docs)




