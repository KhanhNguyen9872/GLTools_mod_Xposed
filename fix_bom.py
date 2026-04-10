import os
for root, _, files in os.walk('app/src/main/java'):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'rb') as f:
                content = f.read()
            if content.startswith(b'\xef\xbb\xbf'):
                print('Removing BOM from', filepath)
                with open(filepath, 'wb') as f:
                    f.write(content[3:])
