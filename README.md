Custom Similarity for Elasticsearch
===================================

The Custom Similarity plugin integrates is based on Tf-Idf similarity (just like DefaultSimilarity)

There are two noticeable changes, though:
- tf is discarded, always returns 1.0f
- by default this Similarity doesn't compress norms to a single byte, so field lengthes are more precise.


To build a `SNAPSHOT` version, you need to build it with Maven:

```bash
mvn clean install
plugin --install custom-similarity \
       --url file:target/releases/custom-similarity-1.0.0-SNAPSHOT.zip
```


Default Configuration
---------------------


```js
{
  "mappings": {
    "my_type": {
      "properties": {
        "my_filed": {
          "type": "string",
          "similarity": "custom"
        }
      }
    }
  }
}
```

Configuration to reactivate compressed norms and discard overlaps:
------------------------------------------------------------------


```js
{
  "settings": {
    "similarity": {
      "pimped_custom" : {
        "type": "custom,
        "compress_norms": false,
        "discard_overlaps": false
      }
    }
  },
  "mappings": {
    "my_type": {
      "properties": {
        "my_filed": {
          "type": "string",
          "similarity": "pimped_custom"
        }
      }
    }
  }
}
```

