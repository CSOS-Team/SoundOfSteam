# First generates transmitter variants by removing the dish
# Then adds the _powered variant
import os
import json

SOURCE_DIR = "src/main/resources/assets/pipeorgans/models/block/musical_link"

BASE_FILES = [
    "receiver.json",
    "receiver_vertical.json",
]

REPLACE_TEXTURES_POWERED = [
    ("pipeorgans:block/musical_link_body", "pipeorgans:block/musical_link_body_powered"),
    ("pipeorgans:block/musical_antenna", "pipeorgans:block/musical_antenna_powered"),
]

def main():
    for base_file in BASE_FILES:
        print(f"Processing {base_file}...")
        base_path = os.path.join(SOURCE_DIR, base_file)
        with open(base_path, 'r') as f:
            base_json = json.load(f)

        # Generate transmitter variant
        transmitter_json = base_json.copy()
        if "elements" in transmitter_json:
            transmitter_json["elements"] = [
                elem for elem in transmitter_json["elements"]
                if not elem.get("name", "").lower().startswith("dish")
            ]

        transmitter_file = base_file.replace("receiver", "transmitter")
        transmitter_path = os.path.join(SOURCE_DIR, transmitter_file)
        with open(transmitter_path, 'w') as f:
            json.dump(transmitter_json, f, indent=4)

        print (f"\tGenerated {transmitter_file}")

        # Generate powered variants
        for variant_json, variant_file in [(base_json, base_file), (transmitter_json, transmitter_file)]:
            powered_json = variant_json.copy()
            if "textures" in powered_json:
                for old_tex, new_tex in REPLACE_TEXTURES_POWERED:
                    for key, value in powered_json["textures"].items():
                        if value == old_tex:
                            powered_json["textures"][key] = new_tex

            powered_file = variant_file.replace(".json", "_powered.json")
            powered_path = os.path.join(SOURCE_DIR, powered_file)
            with open(powered_path, 'w') as f:
                json.dump(powered_json, f, indent=4)

            print(f"\tGenerated {powered_file}")

if __name__ == "__main__":
    main()