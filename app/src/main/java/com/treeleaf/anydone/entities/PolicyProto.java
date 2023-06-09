// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: policy.proto

package com.treeleaf.anydone.entities;

public final class PolicyProto {
  private PolicyProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface PolicyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:treeleaf.anydone.entities.Policy)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional string policyUrl = 1;</code>
     */
    java.lang.String getPolicyUrl();
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    com.google.protobuf.ByteString
        getPolicyUrlBytes();
  }
  /**
   * Protobuf type {@code treeleaf.anydone.entities.Policy}
   */
  public  static final class Policy extends
      com.google.protobuf.GeneratedMessageLite<
          Policy, Policy.Builder> implements
      // @@protoc_insertion_point(message_implements:treeleaf.anydone.entities.Policy)
      PolicyOrBuilder {
    private Policy() {
      policyUrl_ = "";
    }
    public static final int POLICYURL_FIELD_NUMBER = 1;
    private java.lang.String policyUrl_;
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    public java.lang.String getPolicyUrl() {
      return policyUrl_;
    }
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    public com.google.protobuf.ByteString
        getPolicyUrlBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(policyUrl_);
    }
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    private void setPolicyUrl(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      policyUrl_ = value;
    }
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    private void clearPolicyUrl() {
      
      policyUrl_ = getDefaultInstance().getPolicyUrl();
    }
    /**
     * <code>optional string policyUrl = 1;</code>
     */
    private void setPolicyUrlBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      policyUrl_ = value.toStringUtf8();
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!policyUrl_.isEmpty()) {
        output.writeString(1, getPolicyUrl());
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (!policyUrl_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getPolicyUrl());
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.treeleaf.anydone.entities.PolicyProto.Policy parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.treeleaf.anydone.entities.PolicyProto.Policy prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * Protobuf type {@code treeleaf.anydone.entities.Policy}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.treeleaf.anydone.entities.PolicyProto.Policy, Builder> implements
        // @@protoc_insertion_point(builder_implements:treeleaf.anydone.entities.Policy)
        com.treeleaf.anydone.entities.PolicyProto.PolicyOrBuilder {
      // Construct using com.treeleaf.anydone.entities.PolicyProto.Policy.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>optional string policyUrl = 1;</code>
       */
      public java.lang.String getPolicyUrl() {
        return instance.getPolicyUrl();
      }
      /**
       * <code>optional string policyUrl = 1;</code>
       */
      public com.google.protobuf.ByteString
          getPolicyUrlBytes() {
        return instance.getPolicyUrlBytes();
      }
      /**
       * <code>optional string policyUrl = 1;</code>
       */
      public Builder setPolicyUrl(
          java.lang.String value) {
        copyOnWrite();
        instance.setPolicyUrl(value);
        return this;
      }
      /**
       * <code>optional string policyUrl = 1;</code>
       */
      public Builder clearPolicyUrl() {
        copyOnWrite();
        instance.clearPolicyUrl();
        return this;
      }
      /**
       * <code>optional string policyUrl = 1;</code>
       */
      public Builder setPolicyUrlBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setPolicyUrlBytes(value);
        return this;
      }

      // @@protoc_insertion_point(builder_scope:treeleaf.anydone.entities.Policy)
    }
    protected final Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.treeleaf.anydone.entities.PolicyProto.Policy();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          com.treeleaf.anydone.entities.PolicyProto.Policy other = (com.treeleaf.anydone.entities.PolicyProto.Policy) arg1;
          policyUrl_ = visitor.visitString(!policyUrl_.isEmpty(), policyUrl_,
              !other.policyUrl_.isEmpty(), other.policyUrl_);
          if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
              .INSTANCE) {
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

                  policyUrl_ = s;
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
          if (PARSER == null) {    synchronized (com.treeleaf.anydone.entities.PolicyProto.Policy.class) {
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


    // @@protoc_insertion_point(class_scope:treeleaf.anydone.entities.Policy)
    private static final com.treeleaf.anydone.entities.PolicyProto.Policy DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Policy();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static com.treeleaf.anydone.entities.PolicyProto.Policy getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<Policy> PARSER;

    public static com.google.protobuf.Parser<Policy> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
