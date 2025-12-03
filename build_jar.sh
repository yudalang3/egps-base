#!/bin/bash

# ===== 配置区 =====
SOURCE_DIR="src/main/java"
CLASS_OUTPUT="target/classes"
JAR_NAME="egps-base-0.0.1-SNAPSHOT.jar"
OUTPUT_DIR="${1:-/tmp}"  # 默认输出到 /tmp，可传参覆盖

# ===== 打包成 JAR =====
echo "Creating JAR with -C option..."
if ! jar -cf "$JAR_NAME" -C "$CLASS_OUTPUT" .; then
    echo "❌ JAR creation failed!"
    exit 1
fi

# ===== 移动到指定位置 =====
FINAL_JAR_PATH="$OUTPUT_DIR/$JAR_NAME"
mv "$JAR_NAME" "$FINAL_JAR_PATH"

echo "✅ JAR built successfully: $FINAL_JAR_PATH"
