// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: paraphrase.proto

package com.treeleaf.anydone.entities;

public final class ParaphraseProto {
  private ParaphraseProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface ParaphraseGenerateRequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:treeleaf.anydone.entities.ParaphraseGenerateRequest)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>repeated string texts = 1;</code>
     */
    java.util.List<String>
        getTextsList();
    /**
     * <code>repeated string texts = 1;</code>
     */
    int getTextsCount();
    /**
     * <code>repeated string texts = 1;</code>
     */
    java.lang.String getTexts(int index);
    /**
     * <code>repeated string texts = 1;</code>
     */
    com.google.protobuf.ByteString
        getTextsBytes(int index);

    /**
     * <code>optional int64 thresholdNumber = 2;</code>
     */
    long getThresholdNumber();
  }
  /**
   * Protobuf type {@code treeleaf.anydone.entities.ParaphraseGenerateRequest}
   */
  public  static final class ParaphraseGenerateRequest extends
      com.google.protobuf.GeneratedMessageLite<
          ParaphraseGenerateRequest, ParaphraseGenerateRequest.Builder> implements
      // @@protoc_insertion_point(message_implements:treeleaf.anydone.entities.ParaphraseGenerateRequest)
      ParaphraseGenerateRequestOrBuilder {
    private ParaphraseGenerateRequest() {
      texts_ = com.google.protobuf.GeneratedMessageLite.emptyProtobufList();
    }
    private int bitField0_;
    public static final int TEXTS_FIELD_NUMBER = 1;
    private com.google.protobuf.Internal.ProtobufList<String> texts_;
    /**
     * <code>repeated string texts = 1;</code>
     */
    public java.util.List<String> getTextsList() {
      return texts_;
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    public int getTextsCount() {
      return texts_.size();
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    public java.lang.String getTexts(int index) {
      return texts_.get(index);
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    public com.google.protobuf.ByteString
        getTextsBytes(int index) {
      return com.google.protobuf.ByteString.copyFromUtf8(
          texts_.get(index));
    }
    private void ensureTextsIsMutable() {
      if (!texts_.isModifiable()) {
        texts_ =
            com.google.protobuf.GeneratedMessageLite.mutableCopy(texts_);
       }
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    private void setTexts(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureTextsIsMutable();
      texts_.set(index, value);
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    private void addTexts(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureTextsIsMutable();
      texts_.add(value);
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    private void addAllTexts(
        java.lang.Iterable<java.lang.String> values) {
      ensureTextsIsMutable();
      com.google.protobuf.AbstractMessageLite.addAll(
          values, texts_);
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    private void clearTexts() {
      texts_ = com.google.protobuf.GeneratedMessageLite.emptyProtobufList();
    }
    /**
     * <code>repeated string texts = 1;</code>
     */
    private void addTextsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureTextsIsMutable();
      texts_.add(value.toStringUtf8());
    }

    public static final int THRESHOLDNUMBER_FIELD_NUMBER = 2;
    private long thresholdNumber_;
    /**
     * <code>optional int64 thresholdNumber = 2;</code>
     */
    public long getThresholdNumber() {
      return thresholdNumber_;
    }
    /**
     * <code>optional int64 thresholdNumber = 2;</code>
     */
    private void setThresholdNumber(long value) {
      
      thresholdNumber_ = value;
    }
    /**
     * <code>optional int64 thresholdNumber = 2;</code>
     */
    private void clearThresholdNumber() {
      
      thresholdNumber_ = 0L;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      for (int i = 0; i < texts_.size(); i++) {
        output.writeString(1, texts_.get(i));
      }
      if (thresholdNumber_ != 0L) {
        output.writeInt64(2, thresholdNumber_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < texts_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeStringSizeNoTag(texts_.get(i));
        }
        size += dataSize;
        size += 1 * getTextsList().size();
      }
      if (thresholdNumber_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, thresholdNumber_);
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * Protobuf type {@code treeleaf.anydone.entities.ParaphraseGenerateRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest, Builder> implements
        // @@protoc_insertion_point(builder_implements:treeleaf.anydone.entities.ParaphraseGenerateRequest)
        com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequestOrBuilder {
      // Construct using com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>repeated string texts = 1;</code>
       */
      public java.util.List<String>
          getTextsList() {
        return java.util.Collections.unmodifiableList(
            instance.getTextsList());
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public int getTextsCount() {
        return instance.getTextsCount();
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public java.lang.String getTexts(int index) {
        return instance.getTexts(index);
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public com.google.protobuf.ByteString
          getTextsBytes(int index) {
        return instance.getTextsBytes(index);
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public Builder setTexts(
          int index, java.lang.String value) {
        copyOnWrite();
        instance.setTexts(index, value);
        return this;
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public Builder addTexts(
          java.lang.String value) {
        copyOnWrite();
        instance.addTexts(value);
        return this;
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public Builder addAllTexts(
          java.lang.Iterable<java.lang.String> values) {
        copyOnWrite();
        instance.addAllTexts(values);
        return this;
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public Builder clearTexts() {
        copyOnWrite();
        instance.clearTexts();
        return this;
      }
      /**
       * <code>repeated string texts = 1;</code>
       */
      public Builder addTextsBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.addTextsBytes(value);
        return this;
      }

      /**
       * <code>optional int64 thresholdNumber = 2;</code>
       */
      public long getThresholdNumber() {
        return instance.getThresholdNumber();
      }
      /**
       * <code>optional int64 thresholdNumber = 2;</code>
       */
      public Builder setThresholdNumber(long value) {
        copyOnWrite();
        instance.setThresholdNumber(value);
        return this;
      }
      /**
       * <code>optional int64 thresholdNumber = 2;</code>
       */
      public Builder clearThresholdNumber() {
        copyOnWrite();
        instance.clearThresholdNumber();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:treeleaf.anydone.entities.ParaphraseGenerateRequest)
    }
    protected final Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          texts_.makeImmutable();
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest other = (com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest) arg1;
          texts_= visitor.visitList(texts_, other.texts_);
          thresholdNumber_ = visitor.visitLong(thresholdNumber_ != 0L, thresholdNumber_,
              other.thresholdNumber_ != 0L, other.thresholdNumber_);
          if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
              .INSTANCE) {
            bitField0_ |= other.bitField0_;
          }
          return this;
        }
        case MERGE_FROM_STREAM: {
          com.google.protobuf.CodedInputStream input =
              (com.google.protobuf.CodedInputStream) arg0;
          com.google.protobuf.ExtensionRegistryLite extensionRegistry =
              (com.google.protobuf.ExtensionRegistryLite) arg1;
          try {
            boolean done = false;
            while (!done) {
              int tag = input.readTag();
              switch (tag) {
                case 0:
                  done = true;
                  break;
                default: {
                  if (!input.skipField(tag)) {
                    done = true;
                  }
                  break;
                }
                case 10: {
                  String s = input.readStringRequireUtf8();
                  if (!texts_.isModifiable()) {
                    texts_ =
                        com.google.protobuf.GeneratedMessageLite.mutableCopy(texts_);
                  }
                  texts_.add(s);
                  break;
                }
                case 16: {

                  thresholdNumber_ = input.readInt64();
                  break;
                }
              }
            }
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException(e.setUnfinishedMessage(this));
          } catch (java.io.IOException e) {
            throw new RuntimeException(
                new com.google.protobuf.InvalidProtocolBufferException(
                    e.getMessage()).setUnfinishedMessage(this));
          } finally {
          }
        }
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          if (PARSER == null) {    synchronized (com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest.class) {
              if (PARSER == null) {
                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
              }
            }
          }
          return PARSER;
        }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:treeleaf.anydone.entities.ParaphraseGenerateRequest)
    private static final com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ParaphraseGenerateRequest();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateRequest getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<ParaphraseGenerateRequest> PARSER;

    public static com.google.protobuf.Parser<ParaphraseGenerateRequest> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }

  public interface ParaphraseGenerateResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:treeleaf.anydone.entities.ParaphraseGenerateResponse)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional string inputText = 1;</code>
     */
    java.lang.String getInputText();
    /**
     * <code>optional string inputText = 1;</code>
     */
    com.google.protobuf.ByteString
        getInputTextBytes();

    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    java.util.List<String>
        getParaphrasesList();
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    int getParaphrasesCount();
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    java.lang.String getParaphrases(int index);
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    com.google.protobuf.ByteString
        getParaphrasesBytes(int index);
  }
  /**
   * Protobuf type {@code treeleaf.anydone.entities.ParaphraseGenerateResponse}
   */
  public  static final class ParaphraseGenerateResponse extends
      com.google.protobuf.GeneratedMessageLite<
          ParaphraseGenerateResponse, ParaphraseGenerateResponse.Builder> implements
      // @@protoc_insertion_point(message_implements:treeleaf.anydone.entities.ParaphraseGenerateResponse)
      ParaphraseGenerateResponseOrBuilder {
    private ParaphraseGenerateResponse() {
      inputText_ = "";
      paraphrases_ = com.google.protobuf.GeneratedMessageLite.emptyProtobufList();
    }
    private int bitField0_;
    public static final int INPUTTEXT_FIELD_NUMBER = 1;
    private java.lang.String inputText_;
    /**
     * <code>optional string inputText = 1;</code>
     */
    public java.lang.String getInputText() {
      return inputText_;
    }
    /**
     * <code>optional string inputText = 1;</code>
     */
    public com.google.protobuf.ByteString
        getInputTextBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(inputText_);
    }
    /**
     * <code>optional string inputText = 1;</code>
     */
    private void setInputText(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      inputText_ = value;
    }
    /**
     * <code>optional string inputText = 1;</code>
     */
    private void clearInputText() {
      
      inputText_ = getDefaultInstance().getInputText();
    }
    /**
     * <code>optional string inputText = 1;</code>
     */
    private void setInputTextBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      inputText_ = value.toStringUtf8();
    }

    public static final int PARAPHRASES_FIELD_NUMBER = 2;
    private com.google.protobuf.Internal.ProtobufList<String> paraphrases_;
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    public java.util.List<String> getParaphrasesList() {
      return paraphrases_;
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    public int getParaphrasesCount() {
      return paraphrases_.size();
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    public java.lang.String getParaphrases(int index) {
      return paraphrases_.get(index);
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    public com.google.protobuf.ByteString
        getParaphrasesBytes(int index) {
      return com.google.protobuf.ByteString.copyFromUtf8(
          paraphrases_.get(index));
    }
    private void ensureParaphrasesIsMutable() {
      if (!paraphrases_.isModifiable()) {
        paraphrases_ =
            com.google.protobuf.GeneratedMessageLite.mutableCopy(paraphrases_);
       }
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    private void setParaphrases(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureParaphrasesIsMutable();
      paraphrases_.set(index, value);
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    private void addParaphrases(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureParaphrasesIsMutable();
      paraphrases_.add(value);
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    private void addAllParaphrases(
        java.lang.Iterable<java.lang.String> values) {
      ensureParaphrasesIsMutable();
      com.google.protobuf.AbstractMessageLite.addAll(
          values, paraphrases_);
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    private void clearParaphrases() {
      paraphrases_ = com.google.protobuf.GeneratedMessageLite.emptyProtobufList();
    }
    /**
     * <code>repeated string paraphrases = 2;</code>
     */
    private void addParaphrasesBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureParaphrasesIsMutable();
      paraphrases_.add(value.toStringUtf8());
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!inputText_.isEmpty()) {
        output.writeString(1, getInputText());
      }
      for (int i = 0; i < paraphrases_.size(); i++) {
        output.writeString(2, paraphrases_.get(i));
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (!inputText_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getInputText());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < paraphrases_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeStringSizeNoTag(paraphrases_.get(i));
        }
        size += dataSize;
        size += 1 * getParaphrasesList().size();
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * Protobuf type {@code treeleaf.anydone.entities.ParaphraseGenerateResponse}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse, Builder> implements
        // @@protoc_insertion_point(builder_implements:treeleaf.anydone.entities.ParaphraseGenerateResponse)
        com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponseOrBuilder {
      // Construct using com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>optional string inputText = 1;</code>
       */
      public java.lang.String getInputText() {
        return instance.getInputText();
      }
      /**
       * <code>optional string inputText = 1;</code>
       */
      public com.google.protobuf.ByteString
          getInputTextBytes() {
        return instance.getInputTextBytes();
      }
      /**
       * <code>optional string inputText = 1;</code>
       */
      public Builder setInputText(
          java.lang.String value) {
        copyOnWrite();
        instance.setInputText(value);
        return this;
      }
      /**
       * <code>optional string inputText = 1;</code>
       */
      public Builder clearInputText() {
        copyOnWrite();
        instance.clearInputText();
        return this;
      }
      /**
       * <code>optional string inputText = 1;</code>
       */
      public Builder setInputTextBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setInputTextBytes(value);
        return this;
      }

      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public java.util.List<String>
          getParaphrasesList() {
        return java.util.Collections.unmodifiableList(
            instance.getParaphrasesList());
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public int getParaphrasesCount() {
        return instance.getParaphrasesCount();
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public java.lang.String getParaphrases(int index) {
        return instance.getParaphrases(index);
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public com.google.protobuf.ByteString
          getParaphrasesBytes(int index) {
        return instance.getParaphrasesBytes(index);
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public Builder setParaphrases(
          int index, java.lang.String value) {
        copyOnWrite();
        instance.setParaphrases(index, value);
        return this;
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public Builder addParaphrases(
          java.lang.String value) {
        copyOnWrite();
        instance.addParaphrases(value);
        return this;
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public Builder addAllParaphrases(
          java.lang.Iterable<java.lang.String> values) {
        copyOnWrite();
        instance.addAllParaphrases(values);
        return this;
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public Builder clearParaphrases() {
        copyOnWrite();
        instance.clearParaphrases();
        return this;
      }
      /**
       * <code>repeated string paraphrases = 2;</code>
       */
      public Builder addParaphrasesBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.addParaphrasesBytes(value);
        return this;
      }

      // @@protoc_insertion_point(builder_scope:treeleaf.anydone.entities.ParaphraseGenerateResponse)
    }
    protected final Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          paraphrases_.makeImmutable();
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse other = (com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse) arg1;
          inputText_ = visitor.visitString(!inputText_.isEmpty(), inputText_,
              !other.inputText_.isEmpty(), other.inputText_);
          paraphrases_= visitor.visitList(paraphrases_, other.paraphrases_);
          if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
              .INSTANCE) {
            bitField0_ |= other.bitField0_;
          }
          return this;
        }
        case MERGE_FROM_STREAM: {
          com.google.protobuf.CodedInputStream input =
              (com.google.protobuf.CodedInputStream) arg0;
          com.google.protobuf.ExtensionRegistryLite extensionRegistry =
              (com.google.protobuf.ExtensionRegistryLite) arg1;
          try {
            boolean done = false;
            while (!done) {
              int tag = input.readTag();
              switch (tag) {
                case 0:
                  done = true;
                  break;
                default: {
                  if (!input.skipField(tag)) {
                    done = true;
                  }
                  break;
                }
                case 10: {
                  String s = input.readStringRequireUtf8();

                  inputText_ = s;
                  break;
                }
                case 18: {
                  String s = input.readStringRequireUtf8();
                  if (!paraphrases_.isModifiable()) {
                    paraphrases_ =
                        com.google.protobuf.GeneratedMessageLite.mutableCopy(paraphrases_);
                  }
                  paraphrases_.add(s);
                  break;
                }
              }
            }
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException(e.setUnfinishedMessage(this));
          } catch (java.io.IOException e) {
            throw new RuntimeException(
                new com.google.protobuf.InvalidProtocolBufferException(
                    e.getMessage()).setUnfinishedMessage(this));
          } finally {
          }
        }
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          if (PARSER == null) {    synchronized (com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse.class) {
              if (PARSER == null) {
                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
              }
            }
          }
          return PARSER;
        }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:treeleaf.anydone.entities.ParaphraseGenerateResponse)
    private static final com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ParaphraseGenerateResponse();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static com.treeleaf.anydone.entities.ParaphraseProto.ParaphraseGenerateResponse getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<ParaphraseGenerateResponse> PARSER;

    public static com.google.protobuf.Parser<ParaphraseGenerateResponse> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
