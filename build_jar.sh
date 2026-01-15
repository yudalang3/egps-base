#!/bin/bash

# ===== Get script directory (works regardless of where script is called from) =====
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# ===== 配置区 =====
SOURCE_DIR="${SCRIPT_DIR}/src/main/java"
CLASS_OUTPUT="${SCRIPT_DIR}/target/classes"
JAR_NAME="egps-base-0.0.1.jar"
TEMP_JAR="${SCRIPT_DIR}/$JAR_NAME"

# ===== 检查参数 =====
if [ -z "$1" ]; then
    echo "❌ Error: At least one output directory is required!"
    echo "Usage: $0 <output_directory1> [output_directory2] ..."
    exit 1
fi

# ===== 打包成 JAR (只打包一次) =====
echo "Creating JAR at: $TEMP_JAR"
if ! jar -cf "$TEMP_JAR" -C "$CLASS_OUTPUT" .; then
    echo "❌ JAR creation failed!"
    exit 1
fi

echo "✅ JAR created successfully: $TEMP_JAR"

# ===== 复制到所有指定目录 =====
echo "Copying JAR to specified directories..."
for OUTPUT_DIR in "$@"; do
    FINAL_JAR_PATH="$OUTPUT_DIR/$JAR_NAME"
    
    # 检查是否和临时JAR位置相同
    if [ "$(realpath "$FINAL_JAR_PATH" 2>/dev/null)" = "$(realpath "$TEMP_JAR" 2>/dev/null)" ]; then
        echo "  → Skipping: $FINAL_JAR_PATH (same as source)"
        continue
    fi
    
    # 检查目标目录是否存在
    if [ ! -d "$OUTPUT_DIR" ]; then
        echo "  ⚠️  Skipping: $OUTPUT_DIR (directory does not exist)"
        continue
    fi
    
    echo "  → Copying to: $FINAL_JAR_PATH"
    if ! cp "$TEMP_JAR" "$FINAL_JAR_PATH"; then
        echo "    ⚠️  Failed to copy to: $OUTPUT_DIR"
    else
        echo "    ✅ Copied successfully"
    fi
done

echo "✅ All done!"
